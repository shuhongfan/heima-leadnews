<template>
  <div class="tab">
    <!-- TODO: 现在是多个页面切换，应该改成一个页面多个组件切换，这样用户体验会更好些 -->
    <router-link
      v-for="(item, index) in tabs"
      :key="index"
      :to="item.path"
      :class="{ 'active': currentPath === item.path }"
      class="item"
    >{{ item.title }}</router-link>
  </div>
</template>

<script>
export default {
  name: 'FansTabBar',
  data () {
    return {
      currentPath: '',
      tabs: [{
        path: '/fans/index',
        title: '粉丝概况'
      }, {
        path: '/fans/portrait',
        title: '粉丝画像'
      }, {
        path: '/fans/list',
        title: '粉丝列表'
      }]
    }
  },
  created () {
    this.currentPath = this.$route.path
  }
}
</script>

<style lang="scss" scoped>
@import '@/scss/element-variables.scss';

.tab {
  margin-top: 37px;
  margin-bottom: 34px;

  .item {
    margin-left: 30px;
    font-size: 18px;
    color: $--color-text-secondary;
  }

  .active {
    position: relative;
    font-weight: 600;
    color: $--color-primary;
  }

  .active::after {
    content: '\25B2';
    position: absolute;
    top: 20px;
    left: 28px;
    font-size: 12px;
  }
}
</style>
