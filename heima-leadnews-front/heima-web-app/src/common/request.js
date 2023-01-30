var querystring=require("querystring");
var crypto =require('crypto-js')
function Request() {
    this.stream=null;
    this.store = null;
}
Request.prototype={
    setStore : function(store){
        this.store = store
    },
    __check : function(){
        if(!this.stream){
            this.stream = weex.requireModule("stream");
            // // user=1
            // this.store.setToken("eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAADWLQQqEMAwA_5KzPcQ2LfU3iWbZCkIhFVyW_fvGg7cZhvnCPhosIBXzq2gKlYlDypsE0RVDwiI0C9FaIkzQeMCClCNSzDVNYKf4bR8betzdzPWt7WA3Pjc37t1Zr_6cZb7P5g1_fxA93U6AAAAA.vWYfL-u7d2no6iVdqS-DzlD4WcQrSsx_U8gLjvZJQ9Itmlw1zeQLCl4sVZ_4EeU33ExCNCHjuCTPoGay4OYEcw")
        }
        return this.stream;
    },
    // 自动设置设备主键
    postByEquipmentId : function(url,body){
        return this.store.getEquipmentId().then(equipmentId=>{
            body['equipmentId']=equipmentId
            return new Promise((resolve, reject) => {
                this.post(url,body).then((d)=>{
                    resolve(d);
                }).catch((e)=>{
                    reject(e);
                })
            })
        }).catch(e=>{
            return new Promise((resolve, reject) => {
                reject(e);
            })
        })
    },
     // 自动设置设备主键
    getByEquipmentId : function(url,body){
        return this.store.getEquipmentId().then(equipmentId=>{
            body['equipmentId']=equipmentId
            return new Promise((resolve, reject) => {
                this.get(url,body).then((d)=>{
                    resolve(d);
                }).catch((e)=>{
                    reject(e);
                })
            })
        }).catch(e=>{
            return new Promise((resolve, reject) => {
                reject(e);
            })
        })
    },
    __fetch : function(type,path,token,time,parms,body){
        let stream = this.__check()
        return new Promise((resolve, reject) => {
            let temp = {
                method: type,
                url: path,
                type: 'json',
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8',
                    'token': token,
                    't': '' + time,
                    'md': this.sign(parms)
                }
            }
            if(body){
                temp['body'] = JSON.stringify(body)
            }
            stream.fetch(temp, (response) => {
                if (response.status == 200) {
                    resolve(response.data)
                } else {
                    reject(response)
                }
            })
        });
    },
    post : function(path,body,parms){
        let time = new Date().getTime()
        if(parms==undefined)parms={}
        else{
            path = path+"?"+querystring.stringify(parms)
        }
        parms['t']=time
        return this.store.getToken().then(token=>{
            return  this.__fetch('POST',path,token,time,parms,body)
        }).catch(e=>{
            if(e.status){
                return new Promise((resolve, reject) => {
                    reject(e)
                });
            }else{
                return  this.__fetch('POST',path,'',time,parms,body)
            }
        })
    },
    get : function(path,parms){
        if(parms){
            let tmp = querystring.stringify(parms)
            if(path.indexOf("?")==-1){
                tmp="?"+tmp;
            }else{
                tmp="&"+tmp;
            }
            path+=tmp;
        }
        let time = new Date().getTime()
        parms['t']=time
        return this.store.getToken().then(token=>{
            return  this.__fetch('GET',path,token,time,parms)
        }).catch(e=>{
            if(e.status){
                return new Promise((resolve, reject) => {
                    reject(e)
                });
            }else{
                return  this.__fetch('GET',path,'',time,parms)
            }
        })
    },
    sign : function(parms){
        let arr = [];
        for (var key in parms) {
            arr.push(key)
        }
        arr.sort();
        let str = '';
        for (var i in arr) {
            if(str!=''){
                str+="&"
            }
            str += arr[i] + "=" + parms[arr[i]]
        }
        return crypto.MD5(str).toString()
    }
}
export  default new Request()
