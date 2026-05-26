import React from "react";
import "./DeviceImgAttribute.css"
class DeviceImgAttribute extends React.Component{

    constructor(props)
    {
        super(props);
        this.state={};
        this.imgData ={};
         
        this.imgPacketsNr = 0;
        
        this.imgId = "";
        this.constructedData = "";
        this.displayImage = false;

        this.fetchImageChumkFromMessage = this.fetchImageChumkFromMessage.bind(this);
        this.fetchImageChumkFromMessage2 = this.fetchImageChumkFromMessage2.bind(this);
        this.generateValueToDisplay = this.generateValueToDisplay.bind(this);
    }
    // {(this.props.attribute.value.value)?"true":"false"}
    fetchImageChumkFromMessage2()
    {
        this.displayImage = false;
        if(this.props.attribute.value.imgId!== undefined && this.props.attribute.value.imgId!== null){
            if(this.props.attribute.value.imgPacketsNr!== undefined && this.props.attribute.value.imgPacketsNr!== null){
                if(this.props.attribute.value.imgId!== this.imgId)
                {
                    this.imgData = {};
                    this.imgId = this.props.attribute.value.imgId;


                    
                }
                if(this.props.attribute.value.imgPacketsNr!== this.imgPacketsNr)
                {
                    this.imgData = {};
                    this.imgPacketsNr = this.props.attribute.value.imgPacketsNr ;


                    
                }
                if(this.props.attribute.value.imgDataId!== undefined && this.props.attribute.value.imgDataId!== null){
                    if(this.props.attribute.value.imgData!== undefined && this.props.attribute.value.imgData!== null){
                         
                            this.imgData[this.props.attribute.value.imgDataId] = this.props.attribute.value.imgData;
                            this.imgPacketsNr = this.props.attribute.value.imgPacketsNr;

                            ////specialconsole.log("[Data ]" + JSON.stringify(this.imgData));
                            ////specialconsole.log("data length "+Object.keys(this.imgData).length+" "+this.imgPacketsNr)
                    }
                }
            }
        }

        if(Object.keys(this.imgData).length>0 && this.imgPacketsNr === Object.keys(this.imgData).length)
        {
            //specialconsole.log("[Data ]" + JSON.stringify(this.imgData));
            let keys = Object.keys(this.imgData);
           /* keys.sort((a,b)=>{ 
                let ia = parseInt(a);
                let ib = parseInt(b);
                if(ia===NaN)
                {
                    return -1;
                }
                if(ib===NaN)
                {
                    return 1;
                }
                return (ia>ib)?1:(ia<ib)?-1:0;
            });*/
            let str = "";
            let key ;
            for(key in keys)
            {
                //specialconsole.log("key " + keys[key]);
                str= str+this.imgData[keys[key]];
            }
            this.constructedData = str;
            this.displayImage = true;
            //specialconsole.log("[Data constr]" + str);

        }
        

    }
    fetchImageChumkFromMessage()
    {
        //specialconsole.log("[Props ] "+JSON.stringify(this.props.attribute));

        if(this.imgStarted===false)
        {
            if(this.props.attribute.value.imgStart!==undefined && this.props.attribute.value.imgStart!==null)
            {
                 if(this.props.attribute.value.imgPacketsNr!==undefined && this.props.attribute.value.imgPacketsNr!==null)
                {
                    if(this.props.attribute.value.imgStart===true)
                    {
                        if(this.props.attribute.value.imgDataId!==undefined && this.props.attribute.value.imgDataId!==null)
                        {
                            this.imgStarted = true;
                            this.imgProgress = 1;
                            this.imgData[this.props.attribute.value.imgDataId] = this.props.attribute.value.imgData;
                            this.imgPacketsNr = this.props.attribute.value.imgPacketsNr;
                        }
                            

                    }
                }
            }
        }
        else{
            if(this.props.attribute.value.imgStart===false)
            {
                if(this.props.attribute.value.imgDataId!==undefined && this.props.attribute.value.imgDataId!==null)
                {
              
                    this.imgProgress = this.imgProgress+1;
                    this.imgData[this.props.attribute.value.imgDataId] = this.props.attribute.value.imgData;
                 }
                   
                   
                    
                    
            }
        }
        if(this.imgStarted === true && this.imgProgress===this.imgPacketsNr)
                    {
                        this.imgStarted = false;
                        //specialconsole.log("[Img received] "+ this.imgProgress+ " "+this.imgPacketsNr);

                        //specialconsole.log(this.imgData);
                    }
                    //specialconsole.log("[P vs nr] "+ this.imgProgress+ " "+this.imgPacketsNr);
                    //specialconsole.log("[DataReceived] "+this.props.attribute.value.imgData)
        return "";
    }
    // {(this.props.attribute.value.displayValue) }
    generateValueToDisplay()
    {
        if(this.displayImage === true)
        {
            return <img src = {this.constructedData}/>
        }
        else{
            return "No image";        }
    }
    render()
    {
        return(
            <div className="imgAttributeMainDiv">
                {this.fetchImageChumkFromMessage2()}
                            <div className="imgAttributeNameDiv">
                            {this.props.attribute.name+": "} &nbsp;
                                </div>
                            <div className="imgAttributeValueDiv">
                           
                            {this.generateValueToDisplay()}
                                </div>
                 
              
            </div>
        );
    }

}

export default DeviceImgAttribute;