package com.booksharer.util;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.booksharer.entity.BookCommunity;
import com.booksharer.entity.BookCommunityLab;
import com.booksharer.entity.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by DELL on 2016/8/18.
 */
public class Utility {
    private static final String TAG="Utility";
    /**
     * 解析查重结果
     *
     * @param response
     * @return
     */
    public static boolean handleFindRepeateResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                return jsonObject.getBoolean("data");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean handleRegisterResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("state") == 0){
                    Log.d(TAG, String.valueOf(jsonObject.getInt("state")));
                    return true;
                }
                else
                    Log.d(TAG,jsonObject.getString("desc"));
//                    Toast.makeText(MyApplication.getContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;

    }

    public static boolean handleLoginResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("state") == 0){
                    JSONObject userData = new JSONObject(jsonObject.getString("data"));
                    User user = new User();
                    user.setUserName(userData.getString("userName"));
                    user.setEmail(userData.getString("email"));
                    user.setPhone(userData.getString("phone"));
                    user.setPassword(userData.getString("password"));
                    user.setNickName(userData.getString("nickName"));
                    MyApplication.setUser(user);
                    Log.d(TAG, String.valueOf(jsonObject.getInt("state")));
                    Log.d(TAG,user.toString());
                    return true;
                }
                else{
//                    JSONObject userData = new JSONObject(jsonObject.getString("data"));
                    Log.d(TAG,jsonObject.getString("desc"));
//                    Toast.makeText(MyApplication.getContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;

    }


    public static boolean handleAddBookResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("state") == 0) {
                   // jsonObject.getInt("data");//data返回书本的id信息
                    return true;
                } else
                    Toast.makeText(MyApplication.getContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean handleFindBookResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("state") == 0) {
                    //TODO
                    jsonObject.getJSONArray("data");//data返回查找书本数组
                    return true;
                } else
                    Toast.makeText(MyApplication.getContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean handleFindBookInCommunityResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("state") == 0) {
                    //TODO
                    jsonObject.getJSONArray("data");//data返回查找书本数组
                    return true;
                } else
                    Toast.makeText(MyApplication.getContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    public static boolean handleAddUserBookResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("state") == 0) {
                    return true;
                } else
                    Toast.makeText(MyApplication.getContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean handleDelUserBookResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("state") == 0) {
                    return true;
                } else
                    Toast.makeText(MyApplication.getContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    public static boolean handleAddCommunityResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("state") == 0) {
                    //TODO
                    jsonObject.getJSONArray("data");//data返回书圈的id信息
                    return true;
                } else
                    Toast.makeText(MyApplication.getContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    public static boolean handleFindNearCommunityResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("state") == 0) {
                    Gson gson = new Gson();
                    List<BookCommunity> bookCommunities = gson.fromJson(jsonObject.getJSONObject("data").toString(), new TypeToken<List<BookCommunity>>(){}.getType());
                    BookCommunityLab.get(MyApplication.getContext()).appendBookCommunities(bookCommunities);
                    return true;
                } else
                    Toast.makeText(MyApplication.getContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean handleBorrowOrderResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("state") == 0) {
                    return true;
                } else
                    Toast.makeText(MyApplication.getContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean handleAllStateResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("state") == 0) {
                    //data中含有多个借书信息的数组
                    //TODO
                    jsonObject.getJSONArray("data");
                    return true;
                } else
                    Toast.makeText(MyApplication.getContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean handleBorrowAgreeResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("state") == 0) {
                    return true;
                } else
                    Toast.makeText(MyApplication.getContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    public static boolean handleBorrowGetBookResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("state") == 0) {
                    return true;
                } else
                    Toast.makeText(MyApplication.getContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    public static boolean handleBorrowReturnBookResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("state") == 0) {
                    return true;
                } else
                    Toast.makeText(MyApplication.getContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    public static boolean handleCommentAddResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("state") == 0) {
                    return true;
                } else
                    Toast.makeText(MyApplication.getContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean handleCommentDelResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("state") == 0) {
                    return true;
                } else
                    Toast.makeText(MyApplication.getContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean handleCommentFindResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("state") == 0) {
                    //TODO
                    jsonObject.getJSONArray("data");
                    return true;
                } else
                    Toast.makeText(MyApplication.getContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


}
