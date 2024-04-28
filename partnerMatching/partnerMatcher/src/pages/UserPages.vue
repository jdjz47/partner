<script setup lang="ts">

import {useRouter} from "vue-router";
import myAxios from "../plusing/myAxios.js";
import {ref} from "vue";

const user =ref( {
  id:'',
  userAccount:'yupi',
  username:'鱼皮',
  gender:'男',
  profile:'学习java',
  planetCode:'1234',
  email:'1840854390@qq.com',
  avatarUrl:'https://himg.bdimg.com/sys/portraitn/item/public.1.e137c1ac.yS1WqOXfSWEasOYJ2-0pvQ',
  phone:'18569023503',
  createTime:new Date(),
  tags:'java'
});
myAxios.get('/currentUser')
    .then(response=>{
  console.log(response)
      let data=response.data;
      user.value={...data};
      console.log(user.value)
    })
const router = useRouter();
const toEdit = (editKey: string, editName: string, editValue: string) => {
  router.push({
    name: '/UserEdit',
    query: {
      editKey,
      editName,
      editValue
    }
  })
}
</script>

<template>
  <van-cell title="昵称" :value="user?.username"  @click="toEdit('username','昵称',user?.username)"/>
  <van-cell title="账号" :value="user?.userAccount" @click="toEdit('userAccount','账号',user?.userAccount)"/>
    <van-cell title="性别"  @click="toEdit('gender','性别',user?.gender)">
        <span v-if="user.gender==1">男</span>
        <span v-else-if="user.gender==0">女</span>
    </van-cell>
  <van-cell title="邮箱" :value="user?.email" @click="toEdit('email','邮箱',user?.email)"/>
  <van-cell title="兴趣" :value="user?.tags" @click="toEdit('tags','兴趣',user?.tags)"/>
  <van-cell title="电话" :value="user?.phone" @click="toEdit('phone','电话',user?.phone)"/>
  <van-cell title="星球编号" :value="user?.planetCode" @click="toEdit('planetCode','星球编号',user?.planetCode)"/>
<!--  <van-cell title="注册时间" :value="user?.createTime" />-->
  <van-cell title="头像" :value="user?.avatarUrl" >
    <img :src="user?.avatarUrl"/>
  </van-cell>
</template>

<style scoped>

</style>