import { Loading } from  'element-ui'
//导出一个进度条的实例 
class LoadingManage {
   myLoading = null //当前进度的实例
   loadingTimer = null //记录一个弹层的时间戳
   timeInterval = 500 //时间间隔 一个弹层的最小时间间隔 
   openLoading () {
       //打开一个进度条 如果当前有进度条在展示 则不再打开 只保持有一个进度条实例
       if(!this.myLoading) {
          this.loadingTimer =  new Date().getTime() //记录一个时间戳
          this.myLoading = Loading.service() //打开
       }
   }
   closeLoading () {
      if(this.myLoading) {
        let  currentTime =  new Date().getTime()
        if((currentTime - this.loadingTimer) < this.timeInterval){
            //如果还不到时间的间隔 就强制等待
            setTimeout(() => {
               this.closeLoading() //等待关闭
            },100)
        } else {
            this.myLoading.close() //关闭
            this.myLoading = null //置空
            this.loadingTimer = null
        }
      }
   }

}
export default new LoadingManage()