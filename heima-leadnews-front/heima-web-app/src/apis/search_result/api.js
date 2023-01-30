function Api(){
    this.vue;
}
Api.prototype = {
    setVue : function(vue){
        this.vue = vue;
    },
    // 加载
    article_search: function(parms){
        let url = this.vue.$config.urls.get('article_search')
        return this.vue.$request.postByEquipmentId(url,{
            searchWords:parms.keyword,
            pageNum:0,
            tag:parms.tag,
            pageSize:20,
            minBehotTime:parms.minBehotTime
        })
    }
}

export default new Api()
