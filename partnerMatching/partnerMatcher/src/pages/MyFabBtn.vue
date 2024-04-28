<script setup>
import { ref, toRefs } from "vue";
import {} from "vant";
    emits: ["tap"];//抛出的事件名
    const { labels } = toRefs(props);
    let btnNumber = labels.value.length;
    console.log(btnNumber);
    let isShow = ref(false);

    const tapClick = (index) => {
      context.emit("tap", index);
    };

    const tapSubmit = () => {
      isShow.value = !isShow.value;
    };



</script>

<template>
  <div
      class="fabbtn animation"
      :style="{ height: isShow ? (btnNumber + 1) * 50 + btnNumber - 1 + 'px' : '50px' }"
  >
    <div
        class="content animation"
        :style="{ height: isShow ? btnNumber * 50 + btnNumber - 1 + 'px' : '0' }"
    >
      <template v-for="(item, index) in labels" :key="index">
        <div
            v-if="isShow"
            :style="{ color: item.color }"
            class="item item-fab"
            :class="[
            index == 0
              ? 'item-top'
              : index == btnNumber - 1
              ? 'item-bottom'
              : 'item-center',
            isShow ? 'item-fab-active' : '',
          ]"
            @click="tapClick(index)"
        >
          {{ item.label }}
        </div>
      </template>
    </div>
    <div class="item submit" @click="tapSubmit">
      {{ isShow ? "取消" : text }}
    </div>
  </div>
</template>

<style scoped>
.fabbtn {
  position: fixed;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  align-content: center;
  z-index: 10;
  width: 50px;
  height: 50px;
  bottom: 50px;
  right: 30px;
  background: #fff;
  border-radius: 25px;
  box-shadow: 0 0 5px 2px rgba(0, 0, 0, 0.2);
}
.content {
  display: flex;
  flex-direction: column;
  overflow: hidden;
}
.item {
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 14px;
  width: 50px;
  height: 50px;
  color: #909399;
  flex: 1;
}
.item-top {
  height: 55px!important;
  border-top-left-radius: 25px;
  border-top-right-radius: 25px;
  border-bottom: 1px solid #eee;
}
.item-center {
  border-bottom: 1px solid #eee;
}
.item-bottom {
  height: 45px!important;
}
.submit {
  background: #409eff;
  color: #fff;
  border-radius: 25px;
  height: 50px !important;
}
.animation {
  transition-property: height;
  transition-duration: 0.2s;
}

.item-fab {
  opacity: 0;
  transition: opacity 1s;
}
.item-fab-active {
  opacity: 1;
}
</style>