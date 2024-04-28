<template>
    <form action="/">
        <van-search
                v-model="value"
                show-action
                placeholder="请输入搜索关键词"
                @search="onSearch"
                @cancel="onCancel"
        />
    </form>
    <van-divider content-position="left">已选标签</van-divider>

    <van-tag v-for="tag in activeIds" :show="show"  closeable size="medium" type="primary" @close="close(tag)">
        {{tag}}
    </van-tag>
    <van-divider content-position="left">选择标签</van-divider>
    <van-tree-select
        v-model:active-id="activeIds"
        v-model:main-active-index="activeIndex"
        :items="tagList"
    />
  <van-button type="primary" block v-on:click="doSearch">搜索</van-button>
</template>

<script setup>
import { ref } from 'vue';
import { showToast } from 'vant';
import {useRouter} from "vue-router";
        const router=useRouter();
        const value = ref('');
        const onCancel = () => showToast('取消');
        const show = ref(true);
        const close = (tag) => {
            activeIds.value=activeIds.value.filter(item=>{
               return tag!==item
            })
        };

const activeIds = ref([]);
const activeIndex = ref(0);
const tags = [
    {
        text: '性别',
        children: [
            { text: '男', id: '男' },
            { text: '女', id: '女' },
             ],
    },
    {
        text: '状态',
        children: [
            { text: 'java', id: 'java' },
            { text: '实习', id: '实习' },
            { text: '考研', id: '考研' },
        ],
    }
];
  const doSearch=()=>{
    console.log("测试")
      router.push({
        name:"/result",
        query:{
          'tags':activeIds.value
        }
      })
  }
  let tagList=ref([...tags]);
  const onSearch = (val) =>{
    console.log(tags,'tags')
    let Temp_tag=tags.map(item=>{
      let tagItem={...item};
      let temp_children=[...tagItem.children];
      const temp_C=temp_children.filter(tag=>{
        if(tag.text.includes(value.value)){
          return true;
        }else{
          return false;
        }
      })
      tagItem.children=temp_C;
      console.log(item.children,'children测试')
      console.log(tags,"tag")
      return tagItem;
    })
    tagList.value=Temp_tag;
    console.log(tagList.value,"Temp_Tag")
  };

</script>

<style scoped>

</style>