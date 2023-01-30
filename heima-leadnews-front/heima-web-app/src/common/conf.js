const  config = {
    // 注册对应服务名称
    services:{
        app:'app'
    },
    // 请求本地的请求service
    // local:{user:false,article:false,behavior:false,login:false},
    local:{app:true},
    // local:{app:false},
    // 代理前缀
    prefix:{
        server_85:'/server_85'
    },
    // 请求本地的请求service
    urls:{
        load:{url:'article/api/v1/article/load/',sv:'app'},
        loadmore:{url:'article/api/v1/article/loadmore/',sv:'app'},
        loadnew:{url:'article/api/v1/article/loadnew/',sv:'app'},
        loadchannels:{url:'admin/api/v1/channel/channels/',sv:'app'},
        /*load:{url:'api/v2/article/load/',sv:'article'},
        loadmore:{url:'api/v2/article/loadmore/',sv:'article'},
        loadnew:{url:'api/v2/article/loadnew/',sv:'article'},*/
        load_article_info:{url:'article/api/v1/article/load_article_info/',sv:'app'},
        load_article_behavior:{url:'article/api/v1/article/load_article_behavior/',sv:'app'},
        load_search_history:{url:'search/api/v1/history/load/',sv:'app'},
        del_search:{url:'search/api/v1/history/del/',sv:'app'},
        clear_search:{url:'search/api/v1/history/clear/',sv:'app'},
        associate_search:{url:'search/api/v1/associate/search/',sv:'app'},
        // associate_search:{url:'search/api/v2/associate/search/',sv:'app'},
        load_hot_keywords:{url:'search/api/v1/hot_keywords/load/',sv:'app'},
        article_search:{url:'search/api/v1/article/search/search/',sv:'app'},
        // ==========  behavior
        show_behavior:{url:'behavior/api/v1/behavior/show_behavior/',sv:'app'},
        read_behavior:{url:'behavior/api/v1/read_behavior/',sv:'app'},
        forward_behavior:{url:'behavior/api/v1/behavior/forward_behavior/',sv:'app'},
        like_behavior:{url:'behavior/api/v1/likes_behavior/',sv:'app'},
        share_behavior:{url:'behavior/api/v1/share_behavior/',sv:'app'},
        unlike_behavior:{url:'behavior/api/v1/unlike_behavior/',sv:'app'},
        collection_behavior:{url:'article/api/v1/collection_behavior/',sv:'app'},
        // ==========  user
        user_follow:{url:'user/api/v1/user/user_follow/',sv:'app'},
        // ==========  login
        user_login:{url:'user/api/v1/login/login_auth/',sv:'app'},
        // ==========  comment
        load_comment: { url: 'comment/api/v1/comment/load', sv: 'app' },
        like_comment: { url: 'comment/api/v1/comment/like', sv: 'app' },
        save_comment: { url: 'comment/api/v1/comment/save', sv: 'app' },
        // ==========  comment repay
        load_comment_repay: { url: 'comment/api/v1/comment_repay/load', sv: 'app' },
        like_comment_repay: { url: 'comment/api/v1/comment_repay/like', sv: 'app' },
        save_comment_repay: { url: 'comment/api/v1/comment_repay/save', sv: 'app' },
        // 解决多访问地址的问题
        getBase : function(url){
            let sv = url.sv
            // 默认指向85服务器，并指向网关+服务名；否则走本地，不加服务名
            if(config.local[sv]){
                return "/"+sv;
            }else{
                // return config.prefix.server_85+'/'+config.services[sv];
                return config.prefix.server_85;
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
