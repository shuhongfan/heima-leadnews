<template>
  <div class="wrapper" @touchmove.prevent>
    <router-view></router-view>
  </div>
</template>

<script>
import util from "@/utils/font"

export default {
  name: 'HeiMa-App',
  created () {
    util.initIconFont();
    document.title='黑马头条'
  }
}
</script>
