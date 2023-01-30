<template>
    <div class="wxc-tab-page">
        <div class="tab-title-list"
             :style="{ height: '120px',paddingBottom:isIPhoneX?'78px':'0'}">
            <div class="title-item"
                 v-for="(v,index) in tabTitles"
                 :key="index"
                 :ref="'wxc-tab-title-'+index"
                 @click="setPage(index,v.url)"
                 :style="{ width: tabStyles.width +'px', height: tabStyles.height +'px', backgroundColor: currentPage == index ? tabStyles.activeBgColor : tabStyles.bgColor }"
                 :accessible="true"
                 :aria-label="`${v.title?v.title:'标签'+index}`">
                <image  class="img" :src="getFillImg(index)" />
                <text
                        v-if="!titleUseSlot"
                        :style="{ fontSize: tabStyles.fontSize+'px', fontWeight: (currentPage == index && tabStyles.isActiveTitleBold)? 'bold' : 'normal', color: currentPage == index ? tabStyles.activeTitleColor : tabStyles.titleColor, paddingLeft:tabStyles.textPaddingLeft+'px', paddingRight:tabStyles.textPaddingRight+'px'}"
                        class="tab-text">{{v.title}}</text>
                <div class="desc-tag" v-if="v.badge && !titleUseSlot">
                    <text class="desc-text">{{v.badge}}</text>
                </div>
                <div v-if="v.dot && !v.badge && !titleUseSlot" class="dot"></div>
                <slot :name="`tab-title-${index}`" v-if="titleUseSlot"></slot>
            </div>
        </div>
    </div>
</template>

<style scoped>
    .img{
        margin-left: 2px;
        width: 40px;
        height: 40px;
        margin-bottom: 3px;
    }
    .wxc-tab-page {
        width: 750px;
        background-color: #ffffff;
        border-top-color: #ebebeb;
        border-top-width: 1px;
    }
    .tab-title-list {
        flex-direction: row;
        justify-content: space-around;
    }
    .title-item {
        justify-content: center;
        align-items: center;
        border-bottom-style: solid;
    }
    .tab-text {
        lines: 1;
        font-weight: 400;
        font-family: '微软雅黑';
        text-overflow: ellipsis;
    }
    .desc-tag {
        position: absolute;
        top: 10px;
        right: 20px;
        border-bottom-right-radius: 14px;
        border-bottom-left-radius: 0;
        border-top-left-radius: 14px;
        border-top-right-radius: 14px;
        background-color: #FF5E00;
        height: 26px;
        align-items: center;
        justify-content: center;
        padding-left: 6px;
        padding-right: 6px;
    }
    .dot {
        width: 12px;
        height: 12px;
        border-bottom-right-radius: 12px;
        border-bottom-left-radius: 12px;
        border-top-left-radius: 12px;
        border-top-right-radius: 12px;
        position: absolute;
        top: 10px;
        right: 40px;
        background-color: #FF5E00;
    }
    .desc-text {
        font-size: 18px;
        color: #ffffff;
    }
</style>

<script>
    const dom = weex.requireModule('dom');
    const animation = weex.requireModule('animation');
    import { Utils } from 'weex-ui';
    import config from './config';

    export default {
        props: {
            tabTitles: {
                type: Array,
                default: () => (config.tabTitles)
            },
            tabStyles:{
                type: Object,
                default: () => ({
                    bgColor: '#ffffff',
                    titleColor: '#757575',
                    activeTitleColor: '#3296FA',
                    activeBgColor: '#ffffff',
                    activeIconColor:'#3296FA',
                    iconColor:'#757575',
                    isActiveTitleBold: true,
                    iconWidth: 70,
                    iconHeight: 70,
                    width: 160,
                    height: 120,
                    fontSize: 24,
                    activeBottomColor: '#FFC900',
                    activeBottomWidth: 120,
                    activeBottomHeight: 6,
                    textPaddingLeft: 10,
                    textPaddingRight: 10
                })
            },
            titleUseSlot: {
                type: Boolean,
                default: false
            }
        },
        data: () => ({
            currentPage: 0
        }),
        created () {
            this.isIPhoneX = Utils.env.isIPhoneX();
        },
        methods: {
            getFillColor (index){
                if(this.currentPage==index){
                    return this.tabStyles.activeIconColor;
                }else{
                    return this.tabStyles.iconColor;
                }
            },
            getFillImg (index){
                if(this.currentPage==index){
                    return this.tabTitles[index].imgActive;
                }else{
                    return this.tabTitles[index].img;
                }
            },
            setPage(page, url = null, animated = true) {
                this.currentPage = page;
                if(page>0){
                    this.$config.noAction();
                }
            }
        }
    };
</script>
