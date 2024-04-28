<script setup lang="ts">
  import {useRoute} from "vue-router";
  import myAxios from '../plusing/myAxios.js'
  import {ref} from "vue";
  const route=useRoute();
  let tagList=route.query.tags;
  console.log(tagList)
  let userList=ref([{
    username:'yupi',
    profile:'学习java',
    planetCode:'1234',
    tags:['java'],
    avatarUrl:'https://himg.bdimg.com/sys/portraitn/item/public.1.e137c1ac.yS1WqOXfSWEasOYJ2-0pvQ'
  }]);
  myAxios.get('/searchByTags', {
    params: {
      tags: tagList
    }
  }).then(response=>{
    console.log(response.data)
    let data=response.data;
    userList.value=data.map(item=>{
      item.tags=item.tags.split(',');
      return item;
    });
    console.log(data,"data")
    console.log(userList.value,"userList")
  })

</script>

<template>
  <van-card
      v-for="user in userList"
      :desc="user.profile"
      :title="`${user.username} (${user.planetCode})`"
      :thumb="user.avatarUrl"
  >
    <template #tags>
      <van-tag plain v-for="tag in user.tags" style="margin-right: 8px" type="primary">{{tag}}</van-tag>
    </template>
    <template #footer>
      <van-button size="mini">联系我</van-button>
    </template>
  </van-card>

</template>

<style scoped>

</style>