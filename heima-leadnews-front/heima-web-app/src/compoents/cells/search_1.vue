<template>
    <div class="cell-body" @click="onClick">
        <text class="title">{{title}}</text>
        <text class="icon" :style="{'backgroundColor':getColor()}" v-if="tip!=''||type!=''">{{getText()}}</text>
    </div>
</template>

<script>
    export default {
        name: "article_1",
        props:{
            title:{
                type:String,
                default:'显示字'
            },
            tip:{
                type:String,
                default:''
            },
            type:{
                type:String,
                default:''
            }
        },
        methods:{
            getColor : function(){
                if(this.tip=='热'||this.type=='0'){return '#ff1111';}
                if(this.tip=='荐'||this.type=='1'){return '#8fdf19';}
                if(this.tip=='新'||this.type=='2'){return '#db5f19';}
                if(this.tip=='火'||this.type=='3'){return '#af1239';}
                if(this.tip=='精'||this.type=='4'){return '#68da89';}
                if(this.tip=='亮'||this.type=='5'){return '#8f78f5';}
                return '#dbdbdb'
            },
            getText : function(){
                if(this.tip=='热'||this.type=='0'){return '热';}
                if(this.tip=='荐'||this.type=='1'){return '荐';}
                if(this.tip=='新'||this.type=='2'){return '新';}
                if(this.tip=='火'||this.type=='3'){return '火';}
                if(this.tip=='精'||this.type=='4'){return '精';}
                if(this.tip=='亮'||this.type=='5'){return '亮';}
                return ''
            },
            onClick : function(){
                this.$emit("onClick",this.title)
            }
        }
    }
</script>

<style lang="less" scoped>
    @import '../../styles/common';
    .cell-body{
        flex: 1;
       flex-direction: row;
        font-size: 36px;
        align-items: center;
        border-bottom-color: #ebebeb;
        border-bottom-width: 1px;
        border-bottom-style: solid;
        padding: 17px 20px;
        background-color: #ffffff;
    }
    .title{
        flex: 1;
        color: #222222;
    }
    .icon{
        font-size: 24px;
        color: #ffffff;
        background-color: red;
        border-radius: 10px;
        padding: 2px 8px;
    }
</style>
