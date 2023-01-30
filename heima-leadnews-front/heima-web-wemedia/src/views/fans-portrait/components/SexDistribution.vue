<template>
    <el-row :gutter="32">
      <el-col :span="12">
        <div class="content bgc">
          <header>粉丝性别比例</header>
          <section>
            <div class="left" :style="{ width:  +  sexPercentage + '%'}">
              <svg class="icon svg-icon" aria-hidden="true">
                <use xlink:href="#iconicon_fans_male" />
              </svg>&nbsp;&nbsp;男&nbsp;{{ sexPercentage }}%
            </div>
            <div class="right" :style="{ width:  +  (100 - sexPercentage) + '%'}">
              女&nbsp;{{ 100 - sexPercentage }}%&nbsp;&nbsp;<svg class="icon svg-icon" aria-hidden="true">
                <use xlink:href="#iconicon_fans_female" />
              </svg>
            </div>
          </section>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="content bgc">
          <header>粉丝终端分布</header>
          <section>
            <div class="left" :style="{ width:  +  terminalPercentage + '%'}">
              <svg class="icon svg-icon" aria-hidden="true">
                <use xlink:href="#iconicon_fans_android" />
              </svg>&nbsp;&nbsp;android&nbsp;{{ terminalPercentage }}%
            </div>
            <div class="right" :style="{ width:  +  (100 - terminalPercentage) + '%'}">
              iOS&nbsp;{{ 100 - terminalPercentage }}%&nbsp;&nbsp;<svg class="icon svg-icon" aria-hidden="true">
                <use xlink:href="#iconicon_fans_ios" />
              </svg>
            </div>
          </section>
        </div>
      </el-col>
    </el-row>
</template>

<script>
export default {
  data () {
    return {
      sexPercentage: 68,
      terminalPercentage: 35
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/scss/element-variables.scss';

.content {
  height: 138px;
  padding: 30px 30px 0 30px;

  header {
    margin-bottom: 21px;
    color: $--color-text-primary;
    font-size: 18px;
    font-weight: 600;
  }

  header::before {
    content: '\00A0';
    width: 4px;
    height: 18px;
    margin-right: 10px;
    border-radius: 3px;
    background: $--color-primary;
  }

  section {
    display: flex;

    .left {
      line-height: 26px;
      background: $--color-primary;
      margin-right: 3px;
      padding-left: 10px;
      border-radius: 4px 0px 0px 4px;
      color: $--color-white;
    }

    .right {
      line-height: 26px;
      background: #3BD396;
      border-radius: 0px 4px 4px 0px;
      padding-right: 10px;
      text-align: right;
      color: $--color-white;
    }
  }
}
</style>
