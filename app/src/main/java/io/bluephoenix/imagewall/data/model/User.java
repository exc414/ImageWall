package io.bluephoenix.imagewall.data.model;

/**
 * @author Carlos A. Perez Zubizarreta
 */

public class User
{
    private String email;
    private String username;
    private String name;
    private String age;
    private String country;
    private long posts;
    private long followers;
    private long following;
    private String profilePhoto;
    private String userid;

    public User() { }

    /**
     * This constructor is used when the user is first being made. Therefore, the posts,
     * followers, following and profile picture will have no set values. However before
     * inserting into the Firebase Database there must be some value in the fields. This
     * is because if we tried retrieving without values set it could give a null exception.
     *
     * @param email    The user's email.
     * @param username The user's username made up of their first name, a dot and
     *                 their last name.
     * @param name     The full name of the user.
     * @param age      The age of the user.
     * @param country  The country where the user resides.
     */
    public User(String email, String username, String name, String age, String country)
    {
        this.email = email;
        this.username = username;
        this.name = name;
        this.age = age;
        this.country = country;
        this.posts = 0;
        this.followers = 0;
        this.following = 0;
        this.profilePhoto = "nopath";
        this.userid = "0";
    }

    /**
     * This constructor is used to update the user information. It is asserted that
     * all fields will get a value here. Therefore no default values are set.
     *
     * @param email    The user's email.
     * @param username      The user's username made up of their first name, a dot and
     *                      their last name.
     * @param name          The full name of the user.
     * @param age           The age of the user.
     * @param country       The country where the user resides.
     * @param posts         The number of posts the user has made.
     * @param followers     The number of followers/friends the user has.
     * @param following     The number of people the user is followings.
     * @param profilePhoto  The path to the user's profile photo.
     * @param userid        A string of random characters for a unique user value.
     */
    public User(String email, String username, String name, String age, String country,
                long posts, long followers, long following, String profilePhoto,
                String userid)
    {
        this.email = email;
        this.username = username;
        this.name = name;
        this.age = age;
        this.country = country;
        this.posts = posts;
        this.followers = followers;
        this.following = following;
        this.profilePhoto = profilePhoto;
        this.userid = userid;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAge()
    {
        return age;
    }

    public void setAge(String age)
    {
        this.age = age;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getProfilePhoto()
    {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto)
    {
        this.profilePhoto = profilePhoto;
    }

    public long getPosts()
    {
        return posts;
    }

    public void setPosts(long posts)
    {
        this.posts = posts;
    }

    public long getFollowers()
    {
        return followers;
    }

    public void setFollowers(long followers)
    {
        this.followers = followers;
    }

    public long getFollowing()
    {
        return following;
    }

    public void setFollowing(long following)
    {
        this.following = following;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getUserid()
    {
        return userid;
    }

    public void setUserid(String userid)
    {
        this.userid = userid;
    }
}
