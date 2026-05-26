class BlokingQueue
{

    constructor(){
    this.listeners = [];
    this.queue = [];
    this.enqueue = this.enqueue.bind(this);
    this.dequeue = this.dequeue.bind(this);
    this.isEmpty = this.isEmpty.bind(this);
  }
  
   
 dequeue(cb) {
      this.queue.length > 0 ? cb(this.queue.shift()) : this.listeners.push(cb);
    }
    enqueue(value) {
      if (this.listeners.length > 0) {
        this.listeners.shift()(value);
        return;
      }
  
      this.queue.push(value);
    }
  isEmpty()
  {
      return this.queue.length>0;
  }
  
}
export default BlokingQueue;