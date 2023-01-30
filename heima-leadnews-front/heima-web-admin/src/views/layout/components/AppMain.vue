<template>
  <section class="app-main">
    <!-- TODO: 过渡动画不好用 -->
    <transition name="fade-transform" mode="out-in">
      <router-view :key="key"/>
    </transition>
  </section>
</template>

<script>
export default {
  name: 'AppMain',
  computed: {
    key () {
      return this.$route.fullPath
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/scss/element-variables.scss';

.app-main {
  position: relative;
  width: 100%;
  min-height: calc(100vh - 80px);
  overflow: hidden;
  background: $--color-white;
  box-shadow:0px 0px 30px 30px rgba(30,30,50,0.05);
  border-radius:8px;
}
</style>
