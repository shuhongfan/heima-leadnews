function Navigator(){
    this.navigator = []
    this.count = 0
    this.max = 9
    this.temp={}
}
Navigator.prototype= {
    push : function(to,form,next) {
        if (this.temp['path'] != to['path']) {
            this.navigator[this.count] = form
            this.count++;
            if (this.count > this.max) this.count = 0;
        }else{
            this.temp={}
        }
        next()
    },
    back : function(){
        this.count=this.count-1;
        if(this.count<0)this.count=this.max;
        this.temp=this.navigator[this.count]
        return this.temp
    }
}

export default new Navigator();
