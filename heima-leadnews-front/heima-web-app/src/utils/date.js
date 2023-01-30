function FormatDate(){}
FormatDate.prototype= {
    formatDate:function(date, fmt) {
        if (/(y+)/.test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length))
        }
        let o = {
            'M+': date.getMonth() + 1,
            'd+': date.getDate(),
            'h+': date.getHours(),
            'm+': date.getMinutes(),
            's+': date.getSeconds()
        }
        for (let k in o) {
            if (new RegExp(`(${k})`).test(fmt)) {
                let str = o[k] + ''
                fmt = fmt.replace(RegExp.$1, RegExp.$1.length === 1 ? str : this.padLeftZero(str))
            }
        }
        return fmt
    },
    padLeftZero:function (str) {
        return ('00' + str).substr(str.length)
    },
    format10:function(time){
        return this.format13(time*1000);
    },
    format13:function(time){
        let date = new Date(time);
        return this.formatDate(date,'yyyy-MM-dd')
    },
    diffTime:function(time){
        if(time.length==10){
            time = parseInt(time)*1000;
        }
        var nowDate = new Date().getTime(),
            oldDate = new Date(time).getTime(),
            diffTime = parseInt((nowDate - oldDate)/1000,10),
            oneMinute = 60,
            oneHour = 60 * oneMinute,
            oneDay = 24 * oneHour,
            oneMonth = 30 * oneDay,
            oneYear = 12 * oneMonth,
            compareArr = [oneYear,oneMonth,oneDay,oneHour,oneMinute],
            postfix = ['年前','个月前','天前','个小时前','分钟前','1分钟内'],
            diffYear,diffMonth,diffDay,diffHour,diffMinute,len=5;
        for(var i =0; i< len ;i++){
            var diff = Math.floor(diffTime/compareArr[i]);
            if(diff > 0){
                return diff + postfix[i];
            }
            else if(i === len -1 && diff === 0){
                return postfix[len];
            }
        }
    }
}
export  default new FormatDate()
