import HttpRepository from "@/repository/HttpRepository";
import type Login from "@/entity/user/Login";
import {inject, singleton} from "tsyringe";
import type PostWrite from "@/entity/post/PostWrite";
import {plainToClass, plainToInstance} from "class-transformer";
import Post from "@/entity/post/Post";

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

    public getList() {
        return this.httpRepository
            .get({
                path: "/api/posts?page=1&size=3"
            }).then((response) => {
                return plainToInstance(Post, response);
            })
    }
};