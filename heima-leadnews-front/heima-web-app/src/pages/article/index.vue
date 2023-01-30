<template>
  <div class="wrapper">
    <TopBar />
    <web ref="webview" class="webview" :src="staticUrl"></web>
  </div>
</template>

<style scoped>
  .wrapper {
    position: absolute;
    top: 0px;
    right: 0px;
    bottom: 0px;
    left: 0px;
	}

	.webview {
    width: 750px;
    position: absolute;
    top: 90px;
    bottom: 0px;
    right: 0px;
    left: 0px;
  }
</style>

<script>
  import TopBar from '@/compoents/bars/article_top_bar'

  export default {
    name: 'HeiMa-Article',
    components: { TopBar },
    props: ['staticUrl'],
    data() {
        return {}
    },
    methods: {}
  }
</script>