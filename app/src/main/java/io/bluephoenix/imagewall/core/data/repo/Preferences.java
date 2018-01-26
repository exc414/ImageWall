package io.bluephoenix.imagewall.core.data.repo;

import android.content.SharedPreferences;

import io.bluephoenix.imagewall.core.data.model.User;

/**
 * @author Carlos A. Perez Zubizarreta
 */
public class Preferences implements IRepository.Preferences
{
    private SharedPreferences sharedPreferences;
    private final String defaultValue = "Unknown";

    private String email = "email";
    private String username = "username";
    private String name = "name";
    private String website = "website";
    private String country = "country";
    private String posts = "posts";
    private String followers = "followers";
    private String following = "following";
    private String profilePhoto = "profilePhoto";
    private String hasProfileBeenSaved = "hasProfileBeenSaved";

    public Preferences(SharedPreferences sharedPreferences)
    {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public boolean hasProfileBeenSaved()
    {
        return sharedPreferences.getBoolean(hasProfileBeenSaved, false);
    }

    @Override
    public void setProfileInformation(User user)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(hasProfileBeenSaved, true);
        editor.putString(email, user.getEmail());
        editor.putString(username, user.getUsername());
        editor.putString(name, user.getName());
        editor.putString(website, user.getWebsite());
        editor.putString(country, user.getCountry());
        editor.putLong(posts, user.getPosts());
        editor.putLong(followers, user.getFollowers());
        editor.putLong(following, user.getFollowing());
        editor.putString(profilePhoto, user.getProfilePhoto());
        editor.apply();
    }

    @Override
    public User getProfileInformation()
    {
        User user = new User();

        user.setEmail(sharedPreferences.getString(email, defaultValue));
        user.setUsername(sharedPreferences.getString(username, defaultValue));
        user.setName(sharedPreferences.getString(name, defaultValue));
        user.setWebsite(sharedPreferences.getString(website, defaultValue));
        user.setCountry(sharedPreferences.getString(country, defaultValue));
        user.setPosts(sharedPreferences.getLong(posts, 0));
        user.setFollowers(sharedPreferences.getLong(followers, 0));
        user.setFollowing(sharedPreferences.getLong(following, 0));
        user.setProfilePhoto(sharedPreferences.getString(profilePhoto, defaultValue));

        return user;
    }
}
