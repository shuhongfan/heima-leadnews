  <template>
  <el-row :gutter="30">
    <el-col :span="6" v-for="(item, index) in statistList" :key="index">
      <el-row
        type="flex"
        align="middle"
        :class="'box ' + item.className"
        :style="'background: ' + colorPlate1[index]"
      >
        <el-col :span="8">
          <el-row type="flex" justify="center">
            <el-row
              type="flex"
              justify="center"
              align="middle"
              class="img"
              :style="'background: ' + colorPlate2[index]"
            >
              <svg
                class="icon svg-icon"
                aria-hidden="true"
                :style="'color: ' + colorPlate3[index]"
              >
                <use :xlink:href="'#' + item.icon" />
              </svg>
            </el-row>
          </el-row>
        </el-col>
        <el-col :span="16">
          <div :style="'color: ' + colorPlate3[index]">
            <span class="number">{{ item.number }}</span>
            <span class="unit" v-if="item.unit">{{ item.unit }}</span>
          </div>
          <div class="text">{{ item.text }}</div>
        </el-col>
      </el-row>
    </el-col>
  </el-row>
</template>

<script>

export default {
  name: 'DimensionStatist',
  props: ['statistList', 'colorPlate1', 'colorPlate2', 'colorPlate3']
}
</script>

<style lang="scss" scoped>
@import '@/scss/element-variables.scss';

.box {
  height: 116px;
  margin-top: 30px;
  border-radius: 24px;

  .img {
    width: 59px;
    height: 59px;
    border-radius: 50%;
  }

  .icon {
    width: 40px;
    height: 40px;
  }

  .number {
    font-size: 32px;
    font-family: 'DIN-Medium';
  }

  .unit {
    font-size: 14px;
    font-weight: bold;
  }

  .text {
    margin-top: 10px;
    color: $--color-text-secondary;
    font-size: 14px;
    font-weight: 600;
    margin-top: 9px;
  }
}
</style>
