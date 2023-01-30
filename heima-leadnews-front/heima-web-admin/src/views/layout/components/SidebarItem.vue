<template>
  <div class="menu-wrapper">
    <!-- TODO: 菜单禁用时的颜色 -->
    <el-menu-item :index="item.path" :key="item.path" :disabled="item.disabled">
      <svg class="icon svg-icon" aria-hidden="true">
        <use :xlink:href="'#' + item.icon" />
      </svg>
      <span slot="title">{{ item.title }}</span>
    </el-menu-item>
  </div>
</template>

<script>
export default {
  name: 'SidebarItem',
  props: {
    item: {
      type: Object,
      required: true
    }
  }
}
</script>
<style lang="scss" scoped>
.menu-wrapper {
  margin-top: 15px;
  overflow-x: hidden;
}

.is-disabled {
  opacity: 1;
  cursor: pointer;
}
</style>
