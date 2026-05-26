package com.liciot.usermanagementms.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liciot.usermanagementms.dto.*;
import com.liciot.usermanagementms.dto.mapper.entitytodto.DeviceMapper;
import com.liciot.usermanagementms.dto.response.DeviceDTO;
import com.liciot.usermanagementms.dto.response.device.DeviceAccessUsersDTO;
import com.liciot.usermanagementms.dto.response.device.DeviceConfigurationDTO;
import com.liciot.usermanagementms.entity.Device;
import com.liciot.usermanagementms.entity.User;
import com.liciot.usermanagementms.entity.protocol.Attribute;
import com.liciot.usermanagementms.entity.protocol.DeviceConfiguration;
import com.liciot.usermanagementms.repository.DeviceRepository;
import com.liciot.usermanagementms.repository.UserRepository;
import com.liciot.usermanagementms.service.kafka.TopicCreator;
import com.liciot.usermanagementms.service.kafka.producer.KafkaProducer;
import com.liciot.usermanagementms.service.transaction.TransactionHandler;
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Attr;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class DeviceService {
    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DeviceMapper deviceMapper;
    @Autowired
    TransactionHandler transactionHandler;
    @Autowired
    TopicCreator topicCreator;
    @Autowired
    KafkaProducer kafkaProducer;

    @Transactional(isolation = Isolation.SERIALIZABLE)
//ionteresant ca la lucru nu am pus.sie si mergea drept e de fapt ca nu am verifiact ce imi returneaza in b rowser ca problemea acolo apare la serializarea jso
//Foarte frumos funtioneaxza tot ce e aiic cu trANS daac deviceul exista nici macar nu il mi insereaza deci nu da cocnstraij violatikn
    //Hmm nu chiatr asa frumos caci daca exista deviceul cu acel id devj aii face update la admin user deci chia rdaca setariele din db sunt ok el face update deasta nu da un prinary key duplication
///////ca e manageable de catree hiubernate persistance session
//Incarca fara transactional\
    //S-ar putea fara transactional sa nu mearga din cauza ca id nu este autogeneratred
    //Si fara transactional tot asa face ii da update
    //Normal ca ii da update ca doar asa se face si insetrul prin sava si stii ca si inainte fethuiai si poi dadeai save pe entitatea modificata si atentie atunci nu fafeai cu transactional d3eci eara nu era mmanageuita sa zi ci ca nush ce

    //Daca ar fi un force insert
    // //si o ricuk nu trtr ebuie sa te bazezi tu pe db pt asta mai ales ca intrew tine si db e Spring dsata care are trotoata logina d cu db
    //the problem is not in database table constrains at all
    //the problem is solved only by collaboratijng and undersanding hoew Spring data nad hibernate does under the hood

//tot e prob;ema ca daca sunt mai multe instante care incearac sa pune dispozitivul acela pe tnru ei si vor trece de if unul va adauga si reptul vor actualiza trebuie cumvq ac exists si inset sa fie atomice

    //deci ar trebui sa faci inserdul dupa simplul json de configuratie ata t acolo ai tot id si tot ce mai vrei
    //si sa nu uiti de ideea cu factory device
    public DeviceDTO insertDevice(InsertDeviceBody insertDeviceBody)//pot sq scriun nn querry opersonalizat in cacr4 fa insert
    {//dei am solutia ar fi sa mabazez pe key dupliation ex de la DB
        //acum vei verifica daca exista factoryDevice cu idul prezentat
        if (!this.deviceRepository.existsById(insertDeviceBody.getDeviceId())) {
            Optional<User> user = userRepository.findById(insertDeviceBody.getAdminUserId());
            if (user.isPresent()) {

                /*user.get().getAuthorityList().size();
                user.get().getAccessDevices().size();
                user.get().getAdminDevices().size();

                 */

                //DeavcetrertuiernwtwZi asta in controller da lazy ininitalization
//Dar trebuie sa fie transactionala ca sA MEARGA SIZE(); altfel da aici lazy init ex
                Device device = new Device();
                device.setId(insertDeviceBody.getDeviceId());
                device.setAdminUser(user.get());
                device.setConfigurationJSON(insertDeviceBody.getConfigurationJSON());
                //this.deviceRepository.save(device);//merg3e si fara asta daca e transactional
                if (device.getConfigurationJSON() != null) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        String configurationJSON = device.getConfigurationJSON();
                        objectMapper.readValue(configurationJSON, JsonNode.class);
                        DeviceConfiguration deviceConfiguration = objectMapper.readValue(device.getConfigurationJSON(), DeviceConfiguration.class);
                        for (Map.Entry<String, Attribute> attribute : deviceConfiguration.getState().entrySet()) {


                           // System.out.println("Attribute: " + attribute.getKey() + ": " + attribute.getValue().getId());


                        }
                        List toCreateTopics = deviceConfiguration.getState().entrySet().stream().map((attribute) -> {
                            return "device.state." + device.getId().toString().toLowerCase() + "." + attribute.getKey().toLowerCase();
                        }).collect(Collectors.toList());// we will only use gat key because the id key inside attribute is not mandatory

                        toCreateTopics.add("device.event." + device.getId().toString());
                        toCreateTopics.add("device.capability." + device.getId().toString());
                        toCreateTopics.add("device.online." + device.getId().toString());

                        this.topicCreator.createTopics(toCreateTopics);
                        String record = "{\"type\":\"deviceAdded\", \"deviceAdded\":\""+device.getId()+"\"}";
                        this.kafkaProducer.produceRecord("user.event."+user.get().getId(), record);
                        System.out.println(deviceConfiguration.getState().size());//stream().map((attribute)->{System.out.println("Attribute: "+attribute.getId()); return null; });
                    } catch (JsonProcessingException e) {

                        e.printStackTrace();
                    }
                }

                return this.deviceMapper.fromDevice(this.deviceRepository.save(device));
            } else {
                return null;
            }
        } else {
            Device device = this.deviceRepository.getById(insertDeviceBody.getDeviceId());



                if(insertDeviceBody.getConfigurationJSON()!=null)
                {
                    device.setConfigurationJSON(insertDeviceBody.getConfigurationJSON());
                }
            if (device.getConfigurationJSON() != null) {
                //aoci nu actualized confJSON fara sa validez cumva id din conf cu cel din URL( din device )
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    String configurationJSON = device.getConfigurationJSON();
                    objectMapper.readValue(configurationJSON, JsonNode.class);
                    DeviceConfiguration deviceConfiguration = objectMapper.readValue(device.getConfigurationJSON(), DeviceConfiguration.class);
                    for (Map.Entry<String, Attribute> attribute : deviceConfiguration.getState().entrySet()) {


                       // System.out.println("Attribute: " + attribute.getKey() + ": " + attribute.getValue().getId());


                    }
                    List toCreateTopics = deviceConfiguration.getState().entrySet().stream().map((attribute) -> {
                        return "device.state." + device.getId().toString().toLowerCase() + "." + attribute.getKey().toLowerCase();
                    }).collect(Collectors.toList());// we will only use gat key because the id key inside attribute is not mandatory

                    toCreateTopics.add("device.event." + device.getId().toString());
                    toCreateTopics.add("device.capability." + device.getId().toString());
                    toCreateTopics.add("device.online." + device.getId().toString());

                    this.topicCreator.createTopics(toCreateTopics);

                    String confs = device.getConfigurationJSON();
                    confs = confs.replaceAll("\"", "\\\\\"");
                    System.out.println(confs);
                    String record = "{\"type\":\"configurationUpdated\", \"configurationUpdated\":\""+confs+"\"}";
                    this.kafkaProducer.produceRecord("device.event."+device.getId().toString(), record);


                    System.out.println(deviceConfiguration.getState().size());//stream().map((attribute)->{System.out.println("Attribute: "+attribute.getId()); return null; });
                } catch (JsonProcessingException e) {

                    e.printStackTrace();
                }
            }
        }
        return null;

    }
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public DeviceDTO insertDevice(InsertDeviceWithUsernameBody insertDeviceBody)//pot sq scriun nn querry opersonalizat in cacr4 fa insert
    {//dei am solutia ar fi sa mabazez pe key dupliation ex de la DB
        //acum vei verifica daca exista factoryDevice cu idul prezentat
        if (!this.deviceRepository.existsById(insertDeviceBody.getDeviceId())) {
            Optional<User> user = userRepository.findByUsername(insertDeviceBody.getAdminUserUsername());
            if (user.isPresent()) {

                /*user.get().getAuthorityList().size();
                user.get().getAccessDevices().size();
                user.get().getAdminDevices().size();

                 */

                //DeavcetrertuiernwtwZi asta in controller da lazy ininitalization
//Dar trebuie sa fie transactionala ca sA MEARGA SIZE(); altfel da aici lazy init ex
                Device device = new Device();
                device.setId(insertDeviceBody.getDeviceId());
                device.setAdminUser(user.get());
                device.setConfigurationJSON(insertDeviceBody.getConfigurationJSON());
                //this.deviceRepository.save(device);//merg3e si fara asta daca e transactional
                if (device.getConfigurationJSON() != null) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        String configurationJSON = device.getConfigurationJSON();
                        objectMapper.readValue(configurationJSON, JsonNode.class);
                        DeviceConfiguration deviceConfiguration = objectMapper.readValue(device.getConfigurationJSON(), DeviceConfiguration.class);
                        for (Map.Entry<String, Attribute> attribute : deviceConfiguration.getState().entrySet()) {


                            // System.out.println("Attribute: " + attribute.getKey() + ": " + attribute.getValue().getId());


                        }
                        List toCreateTopics = deviceConfiguration.getState().entrySet().stream().map((attribute) -> {
                            return "device.state." + device.getId().toString().toLowerCase() + "." + attribute.getKey().toLowerCase();
                        }).collect(Collectors.toList());// we will only use gat key because the id key inside attribute is not mandatory

                        toCreateTopics.add("device.event." + device.getId().toString());
                        toCreateTopics.add("device.capability." + device.getId().toString());
                        toCreateTopics.add("device.online." + device.getId().toString());

                        this.topicCreator.createTopics(toCreateTopics);
                        String record = "{\"type\":\"deviceAdded\", \"deviceAdded\":\""+device.getId()+"\"}";
                        this.kafkaProducer.produceRecord("user.event."+user.get().getId(), record);
                        System.out.println(deviceConfiguration.getState().size());//stream().map((attribute)->{System.out.println("Attribute: "+attribute.getId()); return null; });
                    } catch (JsonProcessingException e) {

                        e.printStackTrace();
                    }
                }

                return this.deviceMapper.fromDevice(this.deviceRepository.save(device));
            } else {
                return null;
            }
        } else {
            Device device = this.deviceRepository.getById(insertDeviceBody.getDeviceId());



            if(insertDeviceBody.getConfigurationJSON()!=null)
            {
                device.setConfigurationJSON(insertDeviceBody.getConfigurationJSON());
            }
            if (device.getConfigurationJSON() != null) {
                //aoci nu actualized confJSON fara sa validez cumva id din conf cu cel din URL( din device )
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    String configurationJSON = device.getConfigurationJSON();
                    objectMapper.readValue(configurationJSON, JsonNode.class);
                    DeviceConfiguration deviceConfiguration = objectMapper.readValue(device.getConfigurationJSON(), DeviceConfiguration.class);
                    for (Map.Entry<String, Attribute> attribute : deviceConfiguration.getState().entrySet()) {


                        // System.out.println("Attribute: " + attribute.getKey() + ": " + attribute.getValue().getId());


                    }
                    List toCreateTopics = deviceConfiguration.getState().entrySet().stream().map((attribute) -> {
                        return "device.state." + device.getId().toString().toLowerCase() + "." + attribute.getKey().toLowerCase();
                    }).collect(Collectors.toList());// we will only use gat key because the id key inside attribute is not mandatory

                    toCreateTopics.add("device.event." + device.getId().toString());
                    toCreateTopics.add("device.capability." + device.getId().toString());
                    toCreateTopics.add("device.online." + device.getId().toString());

                    this.topicCreator.createTopics(toCreateTopics);

                    String confs = device.getConfigurationJSON();
                    confs = confs.replaceAll("\"", "\\\\\"");
                    System.out.println(confs);
                    String record = "{\"type\":\"configurationUpdated\", \"configurationUpdated\":\""+confs+"\"}";
                    this.kafkaProducer.produceRecord("device.event."+device.getId().toString(), record);


                    System.out.println(deviceConfiguration.getState().size());//stream().map((attribute)->{System.out.println("Attribute: "+attribute.getId()); return null; });
                } catch (JsonProcessingException e) {

                    e.printStackTrace();
                }
            }
        }
        return null;

    }


    @Transactional(isolation = Isolation.SERIALIZABLE)

    public DeviceDTO insertDevice(InsertDeviceWithUsernameByConfigurationBody insertDeviceBody)//pot sq scriun nn querry opersonalizat in cacr4 fa insert
    {


        if (insertDeviceBody.getConfigurationJSON() != null) {

            try {
                ObjectMapper objectMapper = new ObjectMapper();

                String configurationJSON = insertDeviceBody.getConfigurationJSON();
                //objectMapper.readValue(configurationJSON, JsonNode.class);
                DeviceConfiguration deviceConfiguration = objectMapper.readValue(insertDeviceBody.getConfigurationJSON(), DeviceConfiguration.class);

                if (!this.deviceRepository.existsById(UUID.fromString(deviceConfiguration.getId()))) {
                    Optional<User> user = userRepository.findByUsername(insertDeviceBody.getAdminUserUsername());
                    if (user.isPresent()) {


                        Device device = new Device();
                        device.setId(UUID.fromString(deviceConfiguration.getId()));
                        device.setAdminUser(user.get());
                        device.setConfigurationJSON(insertDeviceBody.getConfigurationJSON());
                        //this.deviceRepository.save(device);//merg3e si fara asta daca e transactional
                        for (Map.Entry<String, Attribute> attribute : deviceConfiguration.getState().entrySet()) {


                           // System.out.println("Attribute: " + attribute.getKey() + ": " + attribute.getValue().getId());


                        }
                        List toCreateTopics = deviceConfiguration.getState().entrySet().stream().map((attribute) -> {
                            return "device.state." + device.getId().toString().toLowerCase() + "." + attribute.getKey().toLowerCase();
                        }).collect(Collectors.toList());// we will only use gat key because the id key inside attribute is not mandatory

                        toCreateTopics.add("device.event." + device.getId().toString());
                        toCreateTopics.add("device.capability." + device.getId().toString());
                        toCreateTopics.add("device.online." + device.getId().toString());

                        this.topicCreator.createTopics(toCreateTopics);

                        String record = "{\"type\":\"deviceAdded\", \"deviceAdded\":\""+device.getId()+"\"}";
                        this.kafkaProducer.produceRecord("user.event."+user.get().getId(), record);
                        System.out.println(deviceConfiguration.getState().size());//stream().map((attribute)->{System.out.println("Attribute: "+attribute.getId()); return null; });
                        return this.deviceMapper.fromDevice(this.deviceRepository.save(device));


                } else {
                    return null;
                }
            } else {
                Device device = this.deviceRepository.getById(UUID.fromString(deviceConfiguration.getId()));
                if(insertDeviceBody.getConfigurationJSON()!=null)
                {
                    device.setConfigurationJSON(insertDeviceBody.getConfigurationJSON());
                }
                if (device.getConfigurationJSON() != null) {


                         for (Map.Entry<String, Attribute> attribute : deviceConfiguration.getState().entrySet()) {


                           // System.out.println("Attribute: " + attribute.getKey() + ": " + attribute.getValue().getId());


                        }
                        List toCreateTopics = deviceConfiguration.getState().entrySet().stream().map((attribute) -> {
                            return "device.state." + device.getId().toString().toLowerCase() + "." + attribute.getKey().toLowerCase();
                        }).collect(Collectors.toList());// we will only use gat key because the id key inside attribute is not mandatory

                        toCreateTopics.add("device.event." + device.getId().toString());
                        toCreateTopics.add("device.capability." + device.getId().toString());
                        toCreateTopics.add("device.online." + device.getId().toString());

                        this.topicCreator.createTopics(toCreateTopics);

                    String confs = device.getConfigurationJSON();
                    confs = confs.replaceAll("\"", "\\\\\"");
                    System.out.println(confs);
                    String record = "{\"type\":\"configurationUpdated\", \"configurationUpdated\":\""+confs+"\"}";
                    this.kafkaProducer.produceRecord("device.event."+device.getId().toString(), record);


                    System.out.println(deviceConfiguration.getState().size());//stream().map((attribute)->{System.out.println("Attribute: "+attribute.getId()); return null; });
                    }
                }
            }
                 catch (Exception e) {

                        e.printStackTrace();
                    }
                }


        return null;

    }


    @Transactional(isolation = Isolation.SERIALIZABLE)

    public DeviceDTO insertDevice(InsertDeviceByConfigurationBody insertDeviceBody)//pot sq scriun nn querry opersonalizat in cacr4 fa insert
    {


        if (insertDeviceBody.getConfigurationJSON() != null) {

            try {
                ObjectMapper objectMapper = new ObjectMapper();

                String configurationJSON = insertDeviceBody.getConfigurationJSON();
                //objectMapper.readValue(configurationJSON, JsonNode.class);
                DeviceConfiguration deviceConfiguration = objectMapper.readValue(insertDeviceBody.getConfigurationJSON(), DeviceConfiguration.class);

                if (!this.deviceRepository.existsById(UUID.fromString(deviceConfiguration.getId()))) {
                    Optional<User> user = userRepository.findById(insertDeviceBody.getAdminUserId());
                    if (user.isPresent()) {


                        Device device = new Device();
                        device.setId(UUID.fromString(deviceConfiguration.getId()));
                        device.setAdminUser(user.get());
                        device.setConfigurationJSON(insertDeviceBody.getConfigurationJSON());
                        //this.deviceRepository.save(device);//merg3e si fara asta daca e transactional
                        for (Map.Entry<String, Attribute> attribute : deviceConfiguration.getState().entrySet()) {


                            // System.out.println("Attribute: " + attribute.getKey() + ": " + attribute.getValue().getId());


                        }
                        List toCreateTopics = deviceConfiguration.getState().entrySet().stream().map((attribute) -> {
                            return "device.state." + device.getId().toString().toLowerCase() + "." + attribute.getKey().toLowerCase();
                        }).collect(Collectors.toList());// we will only use gat key because the id key inside attribute is not mandatory

                        toCreateTopics.add("device.event." + device.getId().toString());
                        toCreateTopics.add("device.capability." + device.getId().toString());
                        toCreateTopics.add("device.online." + device.getId().toString());

                        this.topicCreator.createTopics(toCreateTopics);

                        String record = "{\"type\":\"deviceAdded\", \"deviceAdded\":\""+device.getId()+"\"}";
                        this.kafkaProducer.produceRecord("user.event."+user.get().getId(), record);
                        System.out.println(deviceConfiguration.getState().size());//stream().map((attribute)->{System.out.println("Attribute: "+attribute.getId()); return null; });
                        return this.deviceMapper.fromDevice(this.deviceRepository.save(device));


                    } else {
                        return null;
                    }
                } else {
                    Device device = this.deviceRepository.getById(UUID.fromString(deviceConfiguration.getId()));
                    if(insertDeviceBody.getConfigurationJSON()!=null)
                    {
                        device.setConfigurationJSON(insertDeviceBody.getConfigurationJSON());
                    }
                    if (device.getConfigurationJSON() != null) {


                        for (Map.Entry<String, Attribute> attribute : deviceConfiguration.getState().entrySet()) {


                            // System.out.println("Attribute: " + attribute.getKey() + ": " + attribute.getValue().getId());


                        }
                        List toCreateTopics = deviceConfiguration.getState().entrySet().stream().map((attribute) -> {
                            return "device.state." + device.getId().toString().toLowerCase() + "." + attribute.getKey().toLowerCase();
                        }).collect(Collectors.toList());// we will only use gat key because the id key inside attribute is not mandatory

                        toCreateTopics.add("device.event." + device.getId().toString());
                        toCreateTopics.add("device.capability." + device.getId().toString());
                        toCreateTopics.add("device.online." + device.getId().toString());

                        this.topicCreator.createTopics(toCreateTopics);

                        String confs = device.getConfigurationJSON();
                        confs = confs.replaceAll("\"", "\\\\\"");
                        System.out.println(confs);
                        String record = "{\"type\":\"configurationUpdated\", \"configurationUpdated\":\""+confs+"\"}";
                        this.kafkaProducer.produceRecord("device.event."+device.getId().toString(), record);


                        System.out.println(deviceConfiguration.getState().size());//stream().map((attribute)->{System.out.println("Attribute: "+attribute.getId()); return null; });
                    }
                }
            }
            catch (Exception e) {

                e.printStackTrace();
            }
        }


        return null;

    }

    public void removeAccessUsers(AccessUsersForDeviceBody accessUsersForDeviceBody) {
        this.transactionHandler.runInTransaction(() -> {
            Optional<Device> deviceOptional = this.deviceRepository.findById(accessUsersForDeviceBody.getDeviceId());
            if (deviceOptional.isPresent()) {
                Device device = deviceOptional.get();

                for (UUID accessUserId : accessUsersForDeviceBody.getAccessUsersId()) {

                    this.removeAccessUser(device, accessUserId);//nu poti apela de aici nici o metoda transactionala din aceeasi clASA PT ca ea e transactionala prin faptul ca se incapuleaza intro clasa proxy si poti s apekazi corect doar prin bean.metotoda pt ca beanul returneaz proxuul er pe net solutie

                }
                System.out.println(device.getAccessUsers());
                //this.deviceRepository.save(device);
            }
            return null;
        });
    }

    public void removeAccessUser(Device device, UUID accessUserId) {
        //this.addAccessUser(device.get(), accessUserId);//nu poti apela de aici nici o metoda transactionala din aceeasi clASA PT ca ea e transactionala prin faptul ca se incapuleaza intro clasa proxy si poti s apekazi corect doar prin bean.metotoda pt ca beanul returneaz proxuul er pe net solutie
        this.transactionHandler.runInTransaction(() -> {
            Optional<User> userOptional = this.userRepository.findById(accessUserId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                //device.getAccessUsers().add(user);
                user.getAccessDevices().remove(device);
                if(!accessUserId.equals(device.getAdminUser().getId())) {
                    String record = "{\"type\":\"deviceRemoved\", \"deviceRemoved\":\"" + device.getId() + "\"}";
                    this.kafkaProducer.produceRecord("user.event." + accessUserId, record);
                }
            }
            return null;
        });
    }

    // @Transactional(isolation = Isolation.SERIALIZABLE)
    public void addAccessUsers(AccessUsersForDeviceBody accessUsersForDeviceBody) {
        this.transactionHandler.runInTransaction(() -> {
            Optional<Device> deviceOptional = this.deviceRepository.findById(accessUsersForDeviceBody.getDeviceId());
            if (deviceOptional.isPresent()) {
                Device device = deviceOptional.get();

                for (UUID accessUserId : accessUsersForDeviceBody.getAccessUsersId()) {

                    this.addAccessUser(device, accessUserId);//nu poti apela de aici nici o metoda transactionala din aceeasi clASA PT ca ea e transactionala prin faptul ca se incapuleaza intro clasa proxy si poti s apekazi corect doar prin bean.metotoda pt ca beanul returneaz proxuul er pe net solutie

                }
                System.out.println(device.getAccessUsers());
                //this.deviceRepository.save(device);
            }
            return null;
        });
    }

    //you dont make this transactional because it is called from a transactional ontet
    //ypu still cam but to s[ecify the a[[ropiatee propasgation configuration
    //@Transactional(propagation = Propagation.REQUIRED)

    //dar oricum apelando din interiorul clasei nu e trans ca doa r cand o apelezi prin beanul de la sprinf ge trams ca el retuirneaza un proxy
    public void addAccessUser(Device device, UUID accessUserId) {
        //this.addAccessUser(device.get(), accessUserId);//nu poti apela de aici nici o metoda transactionala din aceeasi clASA PT ca ea e transactionala prin faptul ca se incapuleaza intro clasa proxy si poti s apekazi corect doar prin bean.metotoda pt ca beanul returneaz proxuul er pe net solutie
        this.transactionHandler.runInTransaction(() -> {
            Optional<User> userOptional = this.userRepository.findById(accessUserId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                //device.getAccessUsers().add(user); //stiu ca sta nu merge adica nju se adauga doar pe celelat cred ca e n funcite de ceva din entty de la abnotari
                //aici merge ca cumba setul daca este exact acelasi obiect calculeaza in spte un hash si vede ca est eacm acelasi dar daca totui setul da erorare ar trebui sa te asiguri in db cca este unic fapt pentru care trebui sa gestionezi asta cu cheia unica ocmousa
                user.getAccessDevices().add(device);


                if(!accessUserId.equals(device.getAdminUser().getId())) {
                    String record = "{\"type\":\"deviceAdded\", \"deviceAdded\":\"" + device.getId() + "\"}";
                    this.kafkaProducer.produceRecord("user.event." + accessUserId, record);
                }
            }
            return null;
        });
    }
    public void addAccessUserByUsername(String  deviceId, String  username) {
        //this.addAccessUser(device.get(), accessUserId);//nu poti apela de aici nici o metoda transactionala din aceeasi clASA PT ca ea e transactionala prin faptul ca se incapuleaza intro clasa proxy si poti s apekazi corect doar prin bean.metotoda pt ca beanul returneaz proxuul er pe net solutie
       UUID id  = UUID.fromString(deviceId);
        this.transactionHandler.runInTransaction(() -> {

            Optional<Device> device = this.deviceRepository.findById(id);

            Optional<User> userOptional = this.userRepository.findByUsername(username);
            if (userOptional.isPresent() && device.isPresent()) {
                User user = userOptional.get();
                //device.getAccessUsers().add(user); //stiu ca sta nu merge adica nju se adauga doar pe celelat cred ca e n funcite de ceva din entty de la abnotari
                //aici merge ca cumba setul daca este exact acelasi obiect calculeaza in spte un hash si vede ca est eacm acelasi dar daca totui setul da erorare ar trebui sa te asiguri in db cca este unic fapt pentru care trebui sa gestionezi asta cu cheia unica ocmousa
                user.getAccessDevices().add(device.get());
                if(!user.getId().equals(device.get().getAdminUser().getId())) {
                    String record = "{\"type\":\"deviceAdded\", \"deviceAdded\":\"" + device.get().getId() + "\"}";
                    this.kafkaProducer.produceRecord("user.event." + user.getId(), record);
                }
            }
            return null;
        });
    }
    public void removeAccessUserByUsername(String deviceId, String usernames) {
        //this.addAccessUser(device.get(), accessUserId);//nu poti apela de aici nici o metoda transactionala din aceeasi clASA PT ca ea e transactionala prin faptul ca se incapuleaza intro clasa proxy si poti s apekazi corect doar prin bean.metotoda pt ca beanul returneaz proxuul er pe net solutie
        UUID id = UUID.fromString(deviceId);

        this.transactionHandler.runInTransaction(() -> {
            Optional<Device> device = this.deviceRepository.findById(id);
            Optional<User> userOptional = this.userRepository.findByUsername(usernames);
            if (userOptional.isPresent()&& device.isPresent()) {
                User user = userOptional.get();
                //device.getAccessUsers().add(user);
                user.getAccessDevices().remove(device.get());
                if(!user.getId().equals(device.get().getAdminUser().getId())) {
                    String record = "{\"type\":\"deviceRemoved\", \"deviceRemoved\":\"" + device.get().getId() + "\"}";
                    this.kafkaProducer.produceRecord("user.event." + user.getId(), record);
                }
            }
            return null;
        });
    }
    //e frumos transactional
    //in pmapper e fetucieste tot de ce este nevoie
    @Transactional
    public List<DeviceDTO> getAll() {
        return this.deviceRepository.findAll().stream().map(device -> {
            return this.deviceMapper.fromDevice(device);
        }).collect(Collectors.toList());
    }


    @Transactional(isolation = Isolation.SERIALIZABLE)

    public List<Device> findByUserId(UUID userId) {

        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return this.deviceRepository.findByAdminUser(user.get());

        }
        return null;
    }
@Transactional(isolation = Isolation.READ_COMMITTED)
    public DeviceConfigurationDTO getDeviceConfigurationById(UUID deviceId) {
        DeviceConfigurationDTO deviceConfigurationDTO = new DeviceConfigurationDTO();
        Device device = this.deviceRepository.getById(deviceId);
        if (device != null) {
            deviceConfigurationDTO.setConfigurationJSON(device.getConfigurationJSON());
        }

        return deviceConfigurationDTO;

    }
    @Transactional(isolation = Isolation.SERIALIZABLE)
            public DeviceAccessUsersDTO getAccessUsers(String deviceId)
    {
        UUID id = UUID.fromString(deviceId);
        DeviceAccessUsersDTO deviceAccessUsersDTO = new DeviceAccessUsersDTO();
        Device device = this.deviceRepository.getById(id);
        if(device!=null)
        {
            for(User user: device.getAccessUsers())
            {
                deviceAccessUsersDTO.addAccessUser(user.getId().toString(), user.getUsername());
            }
        }

        return deviceAccessUsersDTO;
    }
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void delete(String deviceId)
    {
        UUID id = UUID.fromString(deviceId);
        Optional<Device> deviceOptional = this.deviceRepository.findById(id);
        if(deviceOptional.isPresent())
        {
            this.deviceRepository.delete(deviceOptional.get());
            String record = "{\"type\":\"deviceDeleted\"}";
            this.kafkaProducer.produceRecord("device.event."+deviceOptional.get().getId(), record);

        }

    }

}
