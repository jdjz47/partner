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
myAxios.get('/EditDistance',{
    params:{
       num:8
    }
}).then(response=>{
    console.log(response)
    let data=response.data;
    userList.value=data.map(item=>{
        item.tags=item.tags.split(",");
        return item;
    });
    console.log(data,"data")
    console.log(userList.value,"userList")
})


const scrollToTop = () => {
  // 滚动到顶部的逻辑
  const scrollToTopDuration = 300;
  const scrollStep = -window.scrollY / (scrollToTopDuration / 15);
}

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