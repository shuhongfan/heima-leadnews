const  config = {
    // 注册对应服务名称
    services:{
        article:'ARTICLE',
        behavior:'BEHAVIOR',
        user:'USER',
        login:'LOGIN'
    },
    // 请求本地的请求service
    // local:{user:false,article:false,behavior:false,login:false},
    local:{user:true,article:true,behavior:true,login:true},
    // 代理前缀
    prefix:{
        server_85:'/server_85'
    },
    urls:{
        load:{url:'api/v2/article/load/',sv:'article'},
        loadmore:{url:'api/v2/article/loadmore/',sv:'article'},
        loadnew:{url:'api/v2/article/loadnew/',sv:'article'},
        load_article_info:{url:'api/v1/article/load_article_info/',sv:'article'},
        load_article_behavior:{url:'api/v1/article/load_article_behavior/',sv:'article'},
        load_search_history:{url:'api/v1/article/search/load_search_history/',sv:'article'},
        del_search:{url:'api/v1/article/search/del_search/',sv:'article'},
        clear_search:{url:'api/v1/article/search/clear_search/',sv:'article'},
        associate_search:{url:'api/v1/article/search/associate_search/',sv:'article'},
        load_hot_keywords:{url:'api/v1/article/search/load_hot_keywords/',sv:'article'},
        article_search:{url:'api/v1/article/search/article_search/',sv:'article'},
        // ==========  behavior
        show_behavior:{url:'api/v1/behavior/show_behavior/',sv:'behavior'},
        read_behavior:{url:'api/v1/behavior/read_behavior/',sv:'behavior'},
        forward_behavior:{url:'api/v1/behavior/forward_behavior/',sv:'behavior'},
        like_behavior:{url:'api/v1/behavior/like_behavior/',sv:'behavior'},
        share_behavior:{url:'api/v1/behavior/share_behavior/',sv:'behavior'},
        unlike_behavior:{url:'api/v1/behavior/unlike_behavior/',sv:'behavior'},
        collection_behavior:{url:'api/v1/behavior/collection_behavior/',sv:'behavior'},
        // ==========  user
        user_follow:{url:'api/v1/user/user_follow/',sv:'user'},
        // ==========  login
        user_login:{url:'api/v1/login/login_auth/',sv:'login'},
        // 解决多访问地址的问题
        getBase : function(url){
            let sv = url.sv
            // 默认指向85服务器，并指向网关+服务名；否则走本地，不加服务名
            if(config.local[sv]){
                return "/"+sv;
            }else{
                return config.prefix.server_85+'/'+config.services[sv];
            }
        },
        get:function(name){
            let tmp = config.urls[name];
            if(tmp)
                return config.urls.getBase(tmp)+"/"+tmp.url;
            else
                return name;
        }
    },
    style : {
        main_bg : '#3296fa'
    },
    noAction:function(){
        const modal = weex.requireModule('modal');
        modal.toast({
            message: '该功能暂未实现',
            duration:3
        })
    }

}
export default config
