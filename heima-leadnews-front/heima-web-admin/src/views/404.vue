<template>
  <div class="nofound">
    <img src="@/assets/404/404.png" @click="$router.replace({path:'/'})" alt="">
  </div>
</template>
<style scoped>
  .nofound {
    width: 100%;
    height: 100%
  }
  .nofound img {
    width: 100%;
    height:100%;
  }
</style>
