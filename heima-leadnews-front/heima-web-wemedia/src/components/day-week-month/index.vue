<template>
  <div class="week-month-year">
    <div
      v-for="(item, index) in groupList"
      :key="index"
      :class="'item ' + (checkedIndex === index ? 'is-checked' : '')"
      @click="handleChange(index)"
    >
      {{ item.label }}
    </div>
  </div>
</template>

<script>
export default {
  name: 'DayWeekMonth',
  data () {
    return {
      checkedIndex: 0,
      groupList: [{ label: '今日', value: '0' }, { label: '本周', value: '1' }, { label: '近7天', value: '7' }, { label: '近30天', value: '30' }]
    }
  },
  methods: {
    handleChange (index) {
      if (this.checkedIndex === index) {
        return
      }
      this.checkedIndex = index
      this.$emit('handleChange', this.groupList[index].value)
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/scss/element-variables.scss';

.week-month-year {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 4px;
  background-color: $--background-color-base;
  border-radius: 4px;

  .item {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 28px;
    padding: 0 10px;
    font-size: 14px;
    color: $--color-text-secondary;
    cursor: pointer;
  }

  .is-checked {
    background-color: $--color-white;
    box-shadow: 0px 1px 4px 0px rgba(0, 0, 0, 0.06);
    border-radius: 4px;
    font-weight: 600;
    color: $--color-text-primary;
  }
}
</style>
