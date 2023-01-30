const  config = {
    urls:{
        baseUrl:'/local',
        load:'api/v1/article/load/',
        loadmore:'api/v1/article/loadmore/',
        loadnew:'api/v1/article/loadnew/',
        show_behavior:'api/v1/article/show_behavior/',
        load_article_info:'api/v1/article/load_article_info/',
        // 解决多平台问题
        getBase : function(){
            if(weex.config.env.platform=='Web'){
                return config.urls.baseUrl;
            }else{
                return config.urls.baseUrl;
            }
        },
        get:function(name){
            let tmp = config.urls[name];
            if(tmp)
                return config.urls.getBase()+"/"+tmp;
            else
                return config.urls.getBase()+"/"+name;
        }
    },
    style : {
        main_bg : '#3296fa'
    }

}
export default config
