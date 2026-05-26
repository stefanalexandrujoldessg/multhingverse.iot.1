import React from "react";
import "./DeviceVideoAttribute.css"
class DeviceVideoAttribute extends React.Component{

    constructor(props)
    {
        super(props);
        this.state={
            shouldRender: false
        };
        this.imgData ={};
         
        this.videoPacketsNr = 0;
        
        this.videoId = "";
    

        this.videoRef = React.createRef();
        this.mediaSource = {};
        this.mediaSourceOpened = false;
        this.sourceBuffer = {};
        this.bufferAvailable = false;
        this.extraBuffer = [];
        this.chunkIds = [];
        this.started = false;
        //this.constructedData = "";
        this.displayVideo = false;
        this.finish = false;

         this.fetchImageChumkFromMessage2 = this.fetchImageChumkFromMessage2.bind(this);
         this.handleVideoData = this.handleVideoData.bind(this);
        this.writeToBuffer = this.writeToBuffer.bind(this);
        this.mediaSourceCallback = this.mediaSourceCallback.bind(this);
        this.sourceBufferCallback = this.sourceBufferCallback.bind(this);
        this.resetVideo = this.resetVideo.bind(this);
        this.stringToByteArray = this.stringToByteArray.bind(this);
        this.mediaSourceAddedBuffer = false;
        this.dataSize = 0;

     
    }
    componentDidMount()
    {
        window.MediaSource = window.MediaSource || window.WebKitMediaSource;
        if (!!!window.MediaSource) {
           
            this.setState({shouldRender :false});
        }
        else{
            this.setState({shouldRender :true});

        }
    }
     stringToByteArray(s){
        //specialconsole.log(typeof(s)+" "+s);
        // Otherwise, fall back to 7-bit ASCII only
        var result = new Uint8Array(s.length);
        for (var i=0; i<s.length; i++){
            result[i] = s.charCodeAt(i);/* w ww. ja  v  a 2s . co  m*/
        }
        return result;
        }

    handleVideoData(data,chunkId)
    {
         
       if(this.started === true)
       {
           if(!this.chunkIds.includes(chunkId))
           {
            this.chunkIds.push(chunkId);

        this.extraBuffer.push(data);
        //specialconsole.log(this.extraBuffer.length + this.videoPacketsNr);
        /*
    if(this.extraBuffer.length === this.videoPacketsNr)
{
    //specialconsole.log("finish");
    this.finish = true;
    //let str=  this.extraBuffer.shift()
    //this.writeToBuffer(str);
}
*/
  if(this.chunkIds.length === this.videoPacketsNr)
{
    console.log("finish"+ this.chunkIds.length);
    this.finish = true;
    //let str=  this.extraBuffer.shift()
    //this.writeToBuffer(str);
}

}
       }
    }
    writeToBuffer(data)
    {
        this.bufferAvailable = false;
        let msg = this.stringToByteArray(data);
        this.sourceBuffer.appendBuffer(msg);
        
    }
      mediaSourceCallback(msg) {
          //specialconsole.log("MEdia Callback " + msg);
          if(this.mediaSourceAddedBuffer===false){
              this.mediaSourceAddedBuffer =true;
        this.sourceBuffer = this.mediaSource.addSourceBuffer('video/mp4; codecs="avc1.64000d,mp4a.40.2"');
        this.sourceBuffer.addEventListener('updateend', this.sourceBufferCallback);
        this.mediaSourceOpened = true;
        this.bufferAvailable = true;
        this.writeToBuffer("");

          //specialconsole.log("mediaSourceCallback");
          
          }
      
  }
  sourceBufferCallback() {
    //specialconsole.log("sourceBufferCallback");

    
         if (this.mediaSourceOpened === true && this.extraBuffer.length>0) {
           let str=  this.extraBuffer.shift()
            this.writeToBuffer(str);
           // //specialconsole.log(str);
           // this.extraBuffer = "";

         }
         else{
             if(this.finish=== true)
             {
              console.log("end");
             this.mediaSource.endOfStream();
             }//this.bufferAvailable = true;
             else
             {
               this.writeToBuffer("");
               //this.bufferAvailable = true;

             }

         }
         
      
  
}
    resetVideo()
    {
        console.log(this.videoRef);
        console.log("reset");
        this.chunkIds = [];
        this.extraBuffer = [];
        this.dataSize = 0;
        this.started = true;
        //let video = document.getElementById("myv");
        let video =this.videoRef.current;
        video.addEventListener('error', (e)=>{//specialconsole.log(video.error);//specialconsole.log(e); //specialconsole.log(this.sourceBuffer);
        });
         this.mediaSourceOpened = false;
         this.bufferAvailable = false;
         this.mediaSourceAddedBuffer = false;
         this.finish = false;
         //specialconsole.log("reset");
        this.mediaSource = new MediaSource();
         video.src = window.URL.createObjectURL(this.mediaSource);
        this.mediaSource.addEventListener('sourceopen', ()=>this.mediaSourceCallback("sourceopen"), false);
        this.mediaSource.addEventListener('webkitsourceopen', ()=>this.mediaSourceCallback("webkitsourceopen"), false);
        this.mediaSource.addEventListener('webkitsourceended',()=>this.mediaSourceCallback("webkitsourceended"), false);
    }
     
    fetchImageChumkFromMessage2()
    {

        if(this.props.attribute.value.start!== undefined && this.props.attribute.value.start!== null && this.props.attribute.value.start === true)
        {
            this.resetVideo();


            
        }
        if(this.props.attribute.value.videoPacketsNr!== undefined && this.props.attribute.value.videoPacketsNr!== null )
        {
                this.videoPacketsNr = this.props.attribute.value.videoPacketsNr;

            
        }
                    if(this.props.attribute.value.videoData!== undefined && this.props.attribute.value.videoData!== null){
                        if(this.props.attribute.value.chunkId!== undefined && this.props.attribute.value.chunkId!== null){

                            ////specialconsole.log(this.dataSize);
                           // this.dataSize++;
                            this.handleVideoData(this.props.attribute.value.videoData, this.props.attribute.value.chunkId);

                        }
                    }
            
        
    }
 

        
     
    render()
    {
        return(
            <>
            {(this.state.shouldRender === false && <div className="videoAttributeMainDiv">This device does not support MediaSource API</div>)}
            {(this.state.shouldRender === true && 
            <div className="videoAttributeMainDiv">
                {this.fetchImageChumkFromMessage2()}
                            <div className="videoAttributeNameDiv">
                            {this.props.attribute.name+": "} &nbsp;
                                </div>
                            <div className="videoAttributeValueDiv">
                           <video id = "myv"  ref = {this.videoRef} controls width="99%" style ={{borderRadius:"0.5rem"}}/>
                           
                                </div>
                 
              
            </div>)}
            </>
        );
    }

}

export default DeviceVideoAttribute;