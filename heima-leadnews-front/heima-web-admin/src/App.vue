<template>
  <el-scrollbar class="scrollbar" style="height:100%">
    <div id="app">
      <router-view/>
    </div>
  </el-scrollbar>
</template>
<script>
export default {
  mounted () {
    document.title = '欢迎登录-黑马头条平台管理系统'
  }
}
</script>
<style lang="scss">
@import '@/scss/element-variables.scss';

#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: $--color-text-primary;
  font-size: $--font-size-base;
}

#nav {
  padding: 30px;
  a {
    font-weight: bold;
    color: #2c3e50;
    &.router-link-exact-active {
      color: #42b983;
    }
  }
}

.scrollbar > div {
    overflow-x: hidden;
}
</style>
