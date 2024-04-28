<script setup >

import UserPages from "./UserPages.vue";
import {ref} from "vue";
import MyJoinTeam from "./MyJoinTeam.vue";
import MyCreateTeam from "./MyCreateTeam.vue";

const activeNames = ref(['1']);

</script>

<template>
  <van-collapse v-model="activeNames">
    <van-collapse-item title="个人信息" name="1">
      <UserPages/>
    </van-collapse-item>
    <van-collapse-item title="我加入的队伍" name="2">
    <MyJoinTeam/>
    </van-collapse-item>
    <van-collapse-item title="我创建的队伍" name="3">
      <MyCreateTeam />
    </van-collapse-item>
  </van-collapse>

</template>

<style scoped>

</style>