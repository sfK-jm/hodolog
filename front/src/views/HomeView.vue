<script setup lang="ts">
import PostComponents from "@/components/PostComponents.vue";
import { onMounted, reactive } from "vue";
import { container } from "tsyringe";
import PostRepository from "@/repository/PostRepository";
import Paging from "@/entity/data/Paging";
import type Post from "@/entity/post/Post";

const POST_REPOSITORY = container.resolve(PostRepository);

type StateType = {
  postList: Paging<Post>
}

const state = reactive<StateType>({
  postList: new Paging<Post>()
})

function getList(page = 1) {
  POST_REPOSITORY.getList(page)
      .then((postList) => {
        console.log('>>>>', postList);
        state.postList = postList;
      })
}

onMounted(() => {
  getList();
})

</script>

<template>
  <div class="content">
    <span class="totalCount">게시글 수: {{ state.postList.totalCount }}</span>
    <ul class="posts">
      <li v-for="post in state.postList.items" :key="post.id">
        <PostComponents :post="post"/>
      </li>
    </ul>

    <div class="d-flex justify-content-center">
      <el-pagination
          :background="true"
          v-model:current-page="state.postList.page"
          layout="prev, pager, next"
          :total="state.postList.totalCount"
          :default-page-size="3"
          @current-change="(page: number) => getList(page)"
      />
    </div>

  </div>
</template>

<style scoped lang="scss">
.content {
  padding: 0 1rem 0 1rem;
  margin-bottom: 2rem;
}

.totalCount {
  font-size: 0.88rem;
}

.posts {
  list-style: none;
  padding: 0;

  li {
    margin-bottom: 2.4rem;

    &:last-child {
      margin-bottom: 0;
    }
  }
}
</style>
