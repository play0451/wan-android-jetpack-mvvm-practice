package com.gw.mvvm.wan.core.network

import com.gw.mvvm.wan.data.model.*
import retrofit2.http.*

/**
 * 网络接口
 * @author play0451
 */
interface ApiService {
    companion object {
        const val SERVICE_URL: String = "https://wanandroid.com/"
    }

    /**
     * 登录
     * @param username String   用户名
     * @param password String   密码
     * @return ApiResponse<UserInfo>
     */
    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): ApiResponse<UserInfo>

    /**
     * 注册
     * @param username String   用户名
     * @param password String   密码
     * @param confirmPassword String    确认密码
     * @return ApiResponse<Any?>
     */
    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") confirmPassword: String
    ): ApiResponse<Any?>

    /**
     * 登出
     * @return ApiResponse<Any?>
     */
    @GET("user/logout/json")
    suspend fun logout(): ApiResponse<Any?>

    /**
     * 获取Banner数据
     */
    @GET("banner/json")
    suspend fun getBannerList(): ApiResponse<ArrayList<BannerInfo>>

    /**
     * 获取首页文章列表
     * @param pageIndex Int  页码
     * @return ApiResponse<PageInfo<ArrayList<AriticleInfo>>>
     */
    @GET("article/list/{page}/json")
    suspend fun getHomeAriticleList(@Path("page") pageIndex: Int): ApiResponse<PageInfo<ArrayList<AriticleInfo>>>

    /**
     * 获取首页文章置顶列表
     * @return ApiResponse<PageInfo<ArrayList<AriticleInfo>>>
     */
    @GET("article/top/json")
    suspend fun getHomeTopAriticleList(): ApiResponse<ArrayList<AriticleInfo>>

    /**
     * 收藏文章
     * @param id Int    文章ID
     * @return ApiResponse<Any?>
     */
    @POST("lg/collect/{id}/json")
    suspend fun collect(@Path("id") id: Int): ApiResponse<Any?>

    /**
     * 取消收藏文章
     * @param id Int    文章ID
     * @return ApiResponse<Any?>
     */
    @POST("lg/uncollect_originId/{id}/json")
    suspend fun uncollect(@Path("id") id: Int): ApiResponse<Any?>

    /**
     * 收藏网址
     */
    @POST("lg/collect/addtool/json")
    suspend fun collectUrl(
        @Query("name") name: String,
        @Query("link") url: String
    ): ApiResponse<CollectUrlInfo>

    /**
     * 获取收藏文章数据
     * @param pageIndex Int 页码
     * @return ApiResponse<PageInfo<ArrayList<CollectAriticleInfo>>>
     */
    @GET("lg/collect/list/{page}/json")
    suspend fun getCollectAriticleList(@Path("page") pageIndex: Int): ApiResponse<PageInfo<ArrayList<CollectAriticleInfo>>>

    /**
     * 获取收藏网址数据
     * @return ApiResponse<ArrayList<CollectUrlInfo>>
     */
    @GET("lg/collect/usertools/json")
    suspend fun getCollectUrlList(): ApiResponse<ArrayList<CollectUrlInfo>>

    /**
     * 获取热门搜索词
     * @return ApiResponse<ArrayList<SearchHotKeyInfo>>
     */
    @GET("hotkey/json")
    suspend fun getSearchHotKeys(): ApiResponse<ArrayList<SearchHotKeyInfo>>

    /**
     * 根据关键词搜索
     * @param pageIndex Int 页码
     * @param key String    关键词
     * @return ApiResponse<PageInfo<ArrayList<AriticleInfo>>>
     */
    @POST("article/query/{page}/json")
    suspend fun getSearchResult(
        @Path("page") pageIndex: Int,
        @Query("k") key: String
    ): ApiResponse<PageInfo<ArrayList<AriticleInfo>>>

    /**
     * 获取他人分享文章列表数据
     * @param userId Int    用户ID
     * @param pageIndex Int 页码,从1开始
     * @return ApiResponse<ShareInfo>
     */
    @GET("user/{id}/share_articles/{page}/json")
    suspend fun getShareDataById(
        @Path("id") userId: Int,
        @Path("page") pageIndex: Int
    ): ApiResponse<ShareInfo>

    /**
     * 项目分类标题
     * @return ApiResponse<ArrayList<ClassifyInfo>>
     */
    @GET("project/tree/json")
    suspend fun getProjecTitleList(): ApiResponse<ArrayList<ClassifyInfo>>

    /**
     * 根据分类id获取项目数据
     * @param pageIndex Int 页码
     * @param classifyId Int    分类ID
     * @return ApiResponse<PageInfo<ArrayList<AriticleInfo>>>
     */
    @GET("project/list/{page}/json")
    suspend fun getProjecListByType(
        @Path("page") pageIndex: Int,
        @Query("cid") classifyId: Int
    ): ApiResponse<PageInfo<ArrayList<AriticleInfo>>>

    /**
     * 获取最新项目数据
     * @param pageIndex Int 页码
     * @return ApiResponse<PageInfo<ArrayList<AriticleInfo>>>
     */
    @GET("article/listproject/{page}/json")
    suspend fun getNewProjecList(@Path("page") pageIndex: Int): ApiResponse<PageInfo<ArrayList<AriticleInfo>>>

    /**
     * 广场列表数据
     * @param pageIndex Int
     * @return ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>>
     */
    @GET("user_article/list/{page}/json")
    suspend fun getSquarePlazaList(@Path("page") pageIndex: Int): ApiResponse<PageInfo<ArrayList<AriticleInfo>>>

    /**
     * 每日一问列表数据
     * @param page Int  页码
     * @return ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>>
     */
    @GET("wenda/list/{page}/json")
    suspend fun getSquareAskList(@Path("page") page: Int): ApiResponse<PageInfo<ArrayList<AriticleInfo>>>

    /**
     * 获取广场体系数据
     * @return ApiResponse<ArrayList<SquareSystemInfo>>
     */
    @GET("tree/json")
    suspend fun getSquareSystemList(): ApiResponse<ArrayList<SquareSystemInfo>>

    /**
     * 获取广场体系下的文章数据
     * @param pageIndex Int 页码
     * @param classifyId Int    分类ID
     * @return ApiResponse<PageInfo<ArrayList<AriticleInfo>>>
     */
    @GET("article/list/{page}/json")
    suspend fun getSquareSystemSubChildList(
        @Path("page") pageIndex: Int,
        @Query("cid") classifyId: Int
    ): ApiResponse<PageInfo<ArrayList<AriticleInfo>>>

    /**
     * 获取广场导航数据
     * @return ApiResponse<ArrayList<SquareNavigationInfo>>
     */
    @GET("navi/json")
    suspend fun getSquareNavigationList(): ApiResponse<ArrayList<SquareNavigationInfo>>

    /**
     * 公众号分类
     * @return ApiResponse<ArrayList<ClassifyInfo>>
     */
    @GET("wxarticle/chapters/json")
    suspend fun getOfficialTitleList(): ApiResponse<ArrayList<ClassifyInfo>>

    /**
     * 获取公众号数据
     * @param id Int 分类ID
     * @param pageIndex Int 页码
     * @return ApiResponse<PageInfo<ArrayList<AriticleInfo>>>
     */
    @GET("wxarticle/list/{id}/{page}/json")
    suspend fun getOfficialAriticleList(
        @Path("id") id: Int,
        @Path("page") pageIndex: Int
    ): ApiResponse<PageInfo<ArrayList<AriticleInfo>>>

    /**
     * 添加文章
     * @param title String  文章标题
     * @param link String    文章链接
     * @return ApiResponse<Any?>
     */
    @POST("lg/user_article/add/json")
    @FormUrlEncoded
    suspend fun addAriticle(
        @Field("title") title: String,
        @Field("link") link: String
    ): ApiResponse<Any?>

    /**
     * 获取自己分享的文章列表数据
     * @param pageIndex Int 页码
     * @return ApiResponse<ShareInfo>
     */
    @GET("user/lg/private_articles/{page}/json")
    suspend fun getShareAriticleList(@Path("page") pageIndex: Int): ApiResponse<ShareInfo>

    /**
     * 删除自己分享的文章
     * @param id Int    文章ID
     * @return ApiResponse<Any?>
     */
    @POST("lg/user_article/delete/{id}/json")
    suspend fun deleteShareAriticle(@Path("id") id: Int): ApiResponse<Any?>

    /**
     * 获取当前账户的个人积分
     * @return ApiResponse<UserCoinInfo>
     */
    @GET("lg/coin/userinfo/json")
    suspend fun getUserCoin(): ApiResponse<UserCoinInfo>

    /**
     * 获取积分列表
     * @param pageIndex Int 页码
     * @return ApiResponse<PageInfo<ArrayList<UserCoinInfo>>>
     */
    @GET("coin/rank/{page}/json")
    suspend fun getUserCoinList(@Path("page") pageIndex: Int): ApiResponse<PageInfo<ArrayList<UserCoinInfo>>>

    /**
     * 获取积分记录
     * @param pageIndex Int 页码
     * @return ApiResponse<PageInfo<ArrayList<UserCoinHistoryInfo>>>
     */
    @GET("lg/coin/list/{page}/json")
    suspend fun getUserCoinHistoryList(@Path("page") pageIndex: Int): ApiResponse<PageInfo<ArrayList<UserCoinHistoryInfo>>>

    /**
     * 获取Todo列表数据 根据完成时间排序
     * @param pageIndex Int 页码
     * @return ApiResponse<PageInfo<ArrayList<TodoInfo>>>
     */
    @GET("/lg/todo/v2/list/{page}/json")
    suspend fun getTodoList(@Path("page") pageIndex: Int): ApiResponse<PageInfo<ArrayList<TodoInfo>>>

    /**
     * 添加一个TODO
     * @param title String  标题
     * @param content String    内容
     * @param date String   完成日期
     * @param type Int  类型
     * @param priority Int  优先级
     * @return ApiResponse<Any?>
     */
    @POST("/lg/todo/add/json")
    @FormUrlEncoded
    suspend fun addTodo(
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("date") date: String,
        @Field("type") type: Int,
        @Field("priority") priority: Int
    ): ApiResponse<Any?>

    /**
     * 修改一个TODO
     * @param title String  标题
     * @param content String    内容
     * @param date String   完成日期
     * @param type Int  类型
     * @param priority Int  优先级
     * @param id Int
     * @return ApiResponse<Any?>
     */
    @POST("/lg/todo/update/{id}/json")
    @FormUrlEncoded
    suspend fun updateTodo(
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("date") date: String,
        @Field("type") type: Int,
        @Field("priority") priority: Int,
        @Path("id") id: Int
    ): ApiResponse<Any?>

    /**
     * 删除一个TODO
     * @param id Int    ID
     * @return ApiResponse<Any?>
     */
    @POST("/lg/todo/delete/{id}/json")
    suspend fun deleteTodo(@Path("id") id: Int): ApiResponse<Any?>

    /**
     * 完成一个TODO
     * @param id Int    ID
     * @param status Int    状态
     * @return ApiResponse<Any?>
     */
    @POST("/lg/todo/done/{id}/json")
    @FormUrlEncoded
    suspend fun finishTodo(@Path("id") id: Int, @Field("status") status: Int): ApiResponse<Any?>
}