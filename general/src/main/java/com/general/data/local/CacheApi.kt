package com.general.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.general.utils.BaseUtil.logMessage
import java.util.*

@Entity
class CacheApi {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var url: String? = null
    var params: String? = null
    var beanName: String? = null
    var objectOfArrayBeanName: String? = null
    var date: Long = 0
    var response: String? = null


    companion object {
        fun insertInCache(
            cacheApiDatabase: CacheApiDatabase,
            url: String?,
            params: String,
            beanName: String?,
            objectOfArrayBeanName: String?,
            response: String?
        ) {
            cacheApiDatabase.cacheApiDao()
                ?.deleteByCustom(url, params, beanName, objectOfArrayBeanName)
            val cacheApi = CacheApi()
            cacheApi.url = url
            cacheApi.params = params
            cacheApi.beanName = beanName
            cacheApi.objectOfArrayBeanName = objectOfArrayBeanName
            cacheApi.response = response
            if (objectOfArrayBeanName != null) {
                logMessage("objectOfArrayBeanName", objectOfArrayBeanName)
            }
            cacheApi.date = Date().time
            cacheApiDatabase.cacheApiDao()?.insert(cacheApi)
        } //    public static Object loadDataFromCache(CacheApi cacheApi) {
        //        String result = null;
        //        String beanName = null;
        //
        //        if (cacheApi != null) {
        //            beanName = cacheApi.getBeanName();
        //            result = cacheApi.getResponse();
        //            try {
        //                Class type = Class.forName(beanName);
        //                Gson gson = new Gson();
        //                String objectOfArrayBeanName = cacheApi.getObjectOfArrayBeanName() ;
        //                Object ob=null;
        //                if(objectOfArrayBeanName==null)
        //                {
        //                    ob = (gson.fromJson(result, type));
        //                }
        //                else
        //                {
        //                    if(objectOfArrayBeanName.equals(Category.class.getName()))
        //                    {
        //                        Type listType = new TypeToken<ArrayList<Category>>() {}.getType();
        //                        ob = (gson.fromJson(result, listType));
        //                    }
        //            }
        //
        //                return ob;
        //            } catch (Exception ex) {
        //                logMessage("ExceptionCacheParseStr", ex.toString());
        //            }
        //        }
        //
        //        return null;
        //    }
    }
}