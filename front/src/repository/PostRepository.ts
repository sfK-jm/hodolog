import HttpRepository from "@/repository/HttpRepository";
import type Login from "@/entity/user/Login";
import {inject, singleton} from "tsyringe";
import type PostWrite from "@/entity/post/PostWrite";
import {plainToClass, plainToInstance} from "class-transformer";
import Post from "@/entity/post/Post";
import Paging from "@/entity/data/Paging";

@singleton()
export default class PostRepository{

    constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {
    }

    public write(request: PostWrite){
        return this.httpRepository.post({
            path: '/api/posts',
            body: request,
        })
    }

    public get(postId: number): Promise<Post> {
        return this.httpRepository.get({
            path: `/api/posts/${postId}`,
        }).then((response) => {
            return plainToInstance(Post, response);
        })
    }

    public getList(page: number) {
        return this.httpRepository
            .get({
                path: `/api/posts?page=${page}&size=3`
            }).then((response) => {
                const paging = plainToInstance<Paging<Post>, any>(Paging, response);
                const items = plainToInstance<Post, any[]>(Post, response.items);
                paging.setItems(items);
                return paging;
            })
    }
};