<template>
  <el-image :src="imgSrc">
    <div slot="placeholder" class="image-slot">
      <img class="img" src="@/assets/user/pic_admin_loading@2x.png" />
    </div>
    <div slot="error" class="image-slot">
      <img class="img" src="@/assets/user/pic_admin_fail@2x.png" />
    </div>
  </el-image>
</template>

<script>
export default {
  name: 'UserAuthDetailImage',
  props: ['imgSrc']
}
</script>

<style lang="scss" scoped>
img {
    width: 100%;
    // min-width: 410px;
    margin-top: 30px;
    margin-bottom: 20px;
}
</style>
