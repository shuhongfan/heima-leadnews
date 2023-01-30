<template>
  <div class="app-wrapper">
    <sidebar :collapse="collapse" :class="['sidebar-container',collapse ? 'hidden-slidecontainer': '']"/>
    <div :class="['main-container',collapse ? 'hidden-container': '']">
      <app-main />
    </div>
    <logoutbar />
  </div>
</template>

<script>
import { Sidebar, AppMain, Logoutbar } from './components'
import emitter from '@/utils/event'

export default {
  name: 'Layout',
  components: {
    Sidebar,
    AppMain,
    Logoutbar
  },
  data () {
    return {
      collapse: false
    }
  },
  created () {
    emitter.$on('changeCollapse', () => {
      this.collapse = !this.collapse
    })
  }
}
</script>
<style lang="scss" scoped>
  .hidden-container {
     margin-left: 50px !important;
  }

  .hidden-slidecontainer {
    width:50px !important;
   .logo img{
        display: none;
    }
  }
</style>
