<template>
  <div class="menu-wrapper">
    <template v-if=" item.children && item.children.length > 1">
      <el-submenu :index="item.path" :key="item.path">
        <template slot="title">
          <svg class="icon" aria-hidden="true">
            <use :xlink:href="'#' + item.icon" />
          </svg>
          <span>{{item.title}}</span>
        </template>
        <template v-for="subItem in item.children">
          <el-menu-item
            :index="subItem.path"
            :key="subItem.path"
          >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{{subItem.title }}</el-menu-item>
        </template>
      </el-submenu>
    </template>
    <template v-else>
      <el-menu-item :index="item.path" :key="item.path">
        <svg class="icon svg-icon" aria-hidden="true">
          <use :xlink:href="'#' + item.icon" />
        </svg>
        <span slot="title">{{ item.title }}</span>
      </el-menu-item>
    </template>
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
  },
  data () {
    return {
      onlyOneChild: null
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
