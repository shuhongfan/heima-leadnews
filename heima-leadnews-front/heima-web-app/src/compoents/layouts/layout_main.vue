<template>
    <div class="wrapper">
        <!-- 二级路由视图 -->
        <div class="router-body"><router-view/></div>
        <!-- 底部菜单 -->
        <div class="main-body"><Menu/></div>
    </div>
</template>

<script>
    // 引入底部菜单
    import Menu from "@/compoents/menus/menu";

    export default {
        name: 'HeiMa-Layout-Mian',
        components: {Menu}
    }
</script>

<style scoped>
    .wrapper{
        flex-direction : column;
    }
    .router-body{
        flex: 1;
        flex-direction : column;
    }
    .main-body{
    }
</style>
