<template>
  <div class="sidebar">
    <div class="logo">
      <img width="147" src="@/assets/index/logo_heima_admin@2x.png" alt />
    </div>
    <el-menu class="sidebar-el-menu" :default-active="defaultRoute" router :collapse="collapse">
      <sidebar-item v-for="route in items" :item="route" :key="route.path" />
    </el-menu>
  </div>
</template>

<script>
import SidebarItem from './SidebarItem.vue'
import { MenuData } from '@/constants/menus'
export default {
  props: ['collapse'],
  components: { SidebarItem },
  data () {
    return {
      items: MenuData // 利用配置的路由来进行菜单展示
    }
  },
  computed: {
    defaultRoute () {
      return this.$route.meta.path // 这里是为了active的菜单能够在刷新页面时也同样高亮显示
    }
  }
}
</script>
