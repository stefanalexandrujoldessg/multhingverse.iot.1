package com.liciot.humanms;

import com.liciot.humanms.dto.response.user.UserInitializationDTO;
import com.liciot.humanms.kafka.consumer.KafkaConsumer;
import com.liciot.humanms.kafka.producer.KafkaProducer;
import com.liciot.humanms.service.ApiConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@SpringBootApplication
public class HumanMs {
	static class Car{
		int a;
		int b;
	}
public static ApiConsumer apiConsumer;
	public static KafkaConsumer kafkaConsumer;
	public static KafkaProducer kafkaProducer;
@Autowired
HumanMs(ApiConsumer apiConsumer, KafkaConsumer kafkaConsumer, KafkaProducer kafkaProducer)
{
	HumanMs.apiConsumer = apiConsumer;
	HumanMs.kafkaConsumer = kafkaConsumer;
	HumanMs.kafkaProducer = kafkaProducer;
}
	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(HumanMs.class, args);
		//HttpHeaders headers = new HttpHeaders();
		//headers.put("Authorization", Arrays.asList("Bearer   eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIqIiwiZXhwIjoxNjM5NTc3MTA1LCJpYXQiOjE2Mzk1NTkxMDUsInRva2VuIjoiOTAwZGNmZTItM2E1ZS00M2IwLWJiOWQtMWFmYTA1OWNlNzg3In0.xQT2BsY0qfJDcS1qijaSzTnUFNtXd9QysSg6rDuR1ZqZN4s-FCtRHimAMkw4n9J52JzWdSoJt1KE1ozi_zvEHA"));

		// UserInitializationDTO userInitializationDTO =  apiConsumer.getForObject("http://localhost:9090/crud/user/getByToken/force", UserInitializationDTO.class, headers);

		////specialSystem.out.println(userInitializationDTO.getId());
		////specialSystem.out.println("Admin devices:");
		//for (UUID dev : userInitializationDTO.getAdminDevicesIds())
		//{
		//	//specialSystem.out.println(dev);
		//}
		////specialSystem.out.println("Access devices:");

		//for (UUID dev : userInitializationDTO.getAccessDevicesIds())
		//{
		//	//specialSystem.out.println(dev);
		//}
/*
		Set<Car> carSet = new HashSet<>();
		Car car = new Car();
		car.a=1;
		car.b=2;
		//specialSystem.out.println(carSet.size());

		carSet.add(car );
		//specialSystem.out.println(carSet.size());

		carSet.add(car );
		//specialSystem.out.println(carSet.size());
		car.b=3;
		carSet.add(car );
		//specialSystem.out.println(carSet.size());
		car = new Car();
		car.a=1;
		car.b=3;
		carSet.add(car);
		//specialSystem.out.println(carSet.size());

		car = new Car();
		car.a=1;
		car.b=3;
		carSet.remove(car);
		//specialSystem.out.println(carSet.size());


		//vai ce tare e deci practic nici macar nu se ia dupa valori parac se ia dupa referinta dar tot nu ma pot baza pe asta aclo la acessdevices in human manaement ms
		//dar asta tocumai ca nu e bine acolo la db ca no dACA SE IA DUPAR REFARINTA NU E BINe deloc ca practim am elasi obiect doar cu doua referinte adica doua obiecte cu acelasi datae nu e bine nici aiaic sad sil folosesc



		Thread.sleep(7000);
		Set<String> topics = new HashSet<>();
		topics.add("device.event.12e451a0-bce2-4071-9366-67031141e708");
		HumanMs.kafkaConsumer.addSubscriptionTopics(topics);

		Thread.sleep(7000);
		  topics = new HashSet<>();
		topics.add("device.state.12e451a0-bce2-4071-9366-67031141e708.atr1id");
		HumanMs.kafkaConsumer.addSubscriptionTopics(topics);
		Thread.sleep(5000);
		HumanMs.kafkaConsumer.addSubscriptionTopics(topics);
		Thread.sleep(5000);

		//topics.add("device.event.12e451a0-bce2-4071-9366-67031141e708");

		HumanMs.kafkaConsumer.removeSubscriptionTopics(topics);

		*/
		//minunat deci se creeaza singure topicurile deci practic am vit3eza maxima la publish si nu trebuioe sa imi fac eu griji daca ceva nu a mers bine al creatie se vor face singure la nevoie

		//doar ac crrezA TOPIURILE Cu factorul de replicare 2 ceea ce nujimi place
		// //
		// oricum pui un housekeeper care verifia permannte topicurile
		// //sterge ce nu trebuie
		// //face rep,iipemtru cine nu are
		//
		//
		//ar trebui in metoda AIA de paitin g device sa fiu atent cand crre topicurile si sa ma sigur acolo ca totul este inregula
		//de ASAEMENA te rog d faci un housekeeper care permanant se ocupa d topiucri mana in mana cu usermanagementms
		/*  Set<String> topics = new HashSet<>();
		topics.add("device.state.6985d5d5-a19d-4ffa-80ab-009ac08f88d1.atr1id2");
		HumanMs.kafkaConsumer.addSubscriptionTopics(topics);
		while(true) {
			Thread.sleep(10000);
			HumanMs.kafkaProducer.produceRecord("device.state.6985d5d5-a19d-4ffa-80ab-009ac08f88d1.atr1id1", "heheh1");
			HumanMs.kafkaProducer.produceRecord("device.state.6985d5d5-a19d-4ffa-80ab-009ac08f88d1.atr1id", "heheh2");


		}

		 */
	}
}
