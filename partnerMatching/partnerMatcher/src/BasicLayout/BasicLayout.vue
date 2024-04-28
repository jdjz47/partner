<script setup lang="ts">

import { ref } from 'vue';
import { showToast } from 'vant';
import route from "../config/route.ts";
import {useRouter} from "vue-router";
  const router=useRouter();
  let active = ref(0)
  const onClickRight=(event)=>{
      router.push({path:"/search"})
  }

  const onChange = (active) =>{
    if(active==0){
        router.push({ path: '/Index' })
      showToast(`主页`)
    }
    if(active==1){
      router.push({ path: '/Team' })
      showToast(`队友页`)
    }
    if(active==2){
      router.push({ path: '/Person' })
      showToast(`个人页`)
    }
    active=active;
  };
const onClickLeft=()=>{
  router.back();
}

</script>

<template>
  <van-nav-bar title="伙伴推荐系统" @click-left="onClickLeft" @click-right="onClickRight" left-arrow>
    <template #right  >
      <van-icon name="search"  size="18" />
    </template>
  </van-nav-bar>
  <div style="margin-bottom: 40px">
  <router-view ></router-view>
  </div>
  <van-tabbar v-model="active" @change="onChange">
    <van-tabbar-item icon="home-o">主页</van-tabbar-item>
    <van-tabbar-item icon="search">队友</van-tabbar-item>
    <van-tabbar-item icon="friends-o">个人</van-tabbar-item>
  </van-tabbar>

</template>

<style scoped>

</style>