<script setup>
import {useRouter} from "vue-router";
import TeamListCard from "./Team/TeamListCard.vue";

const router=useRouter();
  const joinTeam=()=>{
    router.push({
      path:"/teamAdd"
    });
  }
</script>

<template>
  <div>
    <van-button type="primary" v-on:click="joinTeam()">加入队伍</van-button>
    <TeamListCard/>
  </div>
</template>

<style scoped>

</style>