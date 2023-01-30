function Cache(){
    this.storage=null;
    this.tokenKey = "TOKEN_KEY"
    this.equipmentidKey = "EQUIPMENTID_KEY"
}
Cache.prototype={
    setToken : function(token){
        return this.__setItem(this.tokenKey,token);
    },
    getToken : function(){
        return this.__getItem(this.tokenKey);
    },
    setEquipmentId : function(equipmentId){
        return this.__setItem(this.equipmentidKey,equipmentId);
    },
    getEquipmentId : function(){
        return this.__getItem(this.equipmentidKey);
    },
    clearToken : function(){
        return this.__removeItem(this.tokenKey);
    },
    __check : function(){
        if(this.storage==null){
            this.storage = weex.requireModule("storage");
            this.setEquipmentId("88888888")
        }
        return this.storage;
    },
    __setItem : function(key,value){
        let storage = this.__check();
        return new Promise((resolve, reject)=>{
            storage.setItem(key, value, function(e){
                if(e.result=='success'){
                    resolve(true)
                }else{
                    reject(false)
                }
            });
        });
    },
    __getItem : function(key){
        let storage = this.__check();
        return new Promise((resolve, reject)=>{
            storage.getItem(key, function(e){
                if(e.result=='success'){
                    resolve(e.data)
                }else{
                    reject(e)
                }
            });
        });
    },
    __removeItem : function(key){
        let storage = this.__check();
        return new Promise((resolve, reject)=>{
            storage.removeItem(key, function(e){
                if(e.result=='success'){
                    resolve(true)
                }else{
                    reject(false)
                }
            });
        });
    }
}
const  che = new Cache();
export  default che
