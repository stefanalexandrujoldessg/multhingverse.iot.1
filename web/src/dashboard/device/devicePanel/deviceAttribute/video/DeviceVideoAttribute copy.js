import React from "react";
import "./DeviceVideoAttribute.css"
class DeviceVideoAttribute extends React.Component{

    constructor(props)
    {
        super(props);
        this.state={};
        this.imgData ={};
         
        this.videoPacketsNr = 0;
        
        this.videoId = "";
    

        this.videoRef = React.createRef();
        this.mediaSource = {};
        this.mediaSourceOpened = false;
        this.sourceBuffer = {};
        this.bufferAvailable = false;
        this.extraBuffer = [];
        this.started = false;
        //this.constructedData = "";
        this.displayVideo = false;

         this.fetchImageChumkFromMessage2 = this.fetchImageChumkFromMessage2.bind(this);
         this.handleVideoData = this.handleVideoData.bind(this);
        this.writeToBuffer = this.writeToBuffer.bind(this);
        this.mediaSourceCallback = this.mediaSourceCallback.bind(this);
        this.sourceBufferCallback = this.sourceBufferCallback.bind(this);
        this.resetVideo = this.resetVideo.bind(this);
        this.stringToByteArray = this.stringToByteArray.bind(this);
        this.mediaSourceAddedBuffer = false;
    }
     stringToByteArray(s){
        console.log(typeof(s)+" "+s);
        // Otherwise, fall back to 7-bit ASCII only
        var result = new Uint8Array(s.length);
        for (var i=0; i<s.length; i++){
            result[i] = s.charCodeAt(i);/* w ww. ja  v  a 2s . co  m*/
        }
        return result;
        }

    handleVideoData(data)
    {
        /*
        if(this.mediaSourceOpened ===true && this.bufferAvailable ===true)
        {
            if(this.extraBuffer.length>0)
            {
                this.extraBuffer = this.extraBuffer+data;
                this.writeToBuffer(this.extraBuffer);
                this.extraBuffer = "";
            }
            else{
                this.writeToBuffer(data);

            }
        }
        else{
            this.extraBuffer = this.extraBuffer+data;
        }
        */
       if(this.started === true)
       {
        this.extraBuffer.push(data);}
    }
    writeToBuffer(data)
    {
        let msg = this.stringToByteArray(data);
        this.sourceBuffer.appendBuffer(msg);
        this.bufferAvailable = false;
    }
      mediaSourceCallback(msg) {
          console.log("MEdia Callback " + msg);
          if(this.mediaSourceAddedBuffer===false){
              this.mediaSourceAddedBuffer =true;
        this.sourceBuffer = this.mediaSource.addSourceBuffer('video/mp4; codecs="avc1.64000d,mp4a.40.2"');
        this.sourceBuffer.addEventListener('updateend', this.sourceBufferCallback);
        this.mediaSourceOpened = true;
        this.bufferAvailable = true;
        this.writeToBuffer("");

          console.log("mediaSourceCallback");
          
          }
      
  }
  sourceBufferCallback() {
    console.log("sourceBufferCallback");

    
         if (this.mediaSourceOpened === true && this.extraBuffer.length>0) {
           let str=  this.extraBuffer.shift()
            this.writeToBuffer(str);
            console.log(str);
           // this.extraBuffer = "";

         }
         else{
             this.writeToBuffer("");
             //this.bufferAvailable = true;
         }
         
      
  
}
    resetVideo()
    {
        this.started = true;
        let video = document.getElementById("myv");
        video.addEventListener('error', (e)=>{console.log(video.error);console.log(e); console.log(this.sourceBuffer);})
         this.mediaSourceOpened = false;
         this.bufferAvailable = false;
         console.log("reset");
        this.mediaSource = new MediaSource();
         video.src = window.URL.createObjectURL(this.mediaSource);
        this.mediaSource.addEventListener('sourceopen', ()=>this.mediaSourceCallback("sourceopen"), false);
        this.mediaSource.addEventListener('webkitsourceopen', ()=>this.mediaSourceCallback("webkitsourceopen"), false);
        this.mediaSource.addEventListener('webkitsourceended',()=>this.mediaSourceCallback("webkitsourceended"), false);
    }
    // {(this.props.attribute.value.value)?"true":"false"}
    fetchImageChumkFromMessage2()
    {
        if(this.props.attribute.value.start!== undefined && this.props.attribute.value.start!== null && this.props.attribute.value.start === true)
        {
            this.resetVideo();


            
        }
        /*
        if(this.props.attribute.value.videoId!== undefined && this.props.attribute.value.videoId!== null){
            if(this.props.attribute.value.videoPacketsNr!== undefined && this.props.attribute.value.videoPacketsNr!== null){
                if(this.props.attribute.value.videoId!== this.videoId)
                {
                    //this.resetVideo();
                    this.videoId = this.props.attribute.value.videoId;


                    
                }
                if(this.props.attribute.value.videoPacketsNr!== this.videoPacketsNr)
                {
                   // this.resetVideo();
                    this.videoPacketsNr = this.props.attribute.value.videoPacketsNr;


                    
                }
               */
                    if(this.props.attribute.value.videoData!== undefined && this.props.attribute.value.videoData!== null){
                         
                            this.handleVideoData(this.props.attribute.value.videoData);

                            ////specialconsole.log("[Data ]" + JSON.stringify(this.imgData));
                            ////specialconsole.log("data length "+Object.keys(this.imgData).length+" "+this.imgPacketsNr)
                    }
            
        
    }
/*
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
           
           

        
     
    render()
    {
        return(
            <div className="videoAttributeMainDiv">
                {this.fetchImageChumkFromMessage2()}
                            <div className="videoAttributeNameDiv">
                            {this.props.attribute.name+": "} &nbsp;
                                </div>
                            <div className="videoAttributeValueDiv">
                           <video id = "myv"   controls width="200px"/>
                           
                                </div>
                 
              
            </div>
        );
    }

}

export default DeviceVideoAttribute;