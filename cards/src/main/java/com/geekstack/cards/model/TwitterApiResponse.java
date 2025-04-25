package com.geekstack.cards.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "TwitterApiResponse")
public class TwitterApiResponse {
    public List<Tweet> data;
    public List<ErrorDetail> errors;
    public Includes includes;
    public Meta meta;

    public List<Tweet> getData() {
        return data;
    }

    public void setData(List<Tweet> data) {
        this.data = data;
    }

    public List<ErrorDetail> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorDetail> errors) {
        this.errors = errors;
    }

    public Includes getIncludes() {
        return includes;
    }

    public void setIncludes(Includes includes) {
        this.includes = includes;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}

class Tweet {
    public String author_id;
    public String created_at;
    public String id;
    public String text;
    public String username;

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

class ErrorDetail {
    public String detail;
    public int status;
    public String title;
    public String type;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

class Includes {
    public List<Media> media;
    public List<Place> places;
    public List<Poll> polls;
    public List<Topic> topics;
    public List<Tweet> tweets;
    public List<User> users;

    public List<Media> getMedia() {
        return media;
    }

    public void setMedia(List<Media> media) {
        this.media = media;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public List<Poll> getPolls() {
        return polls;
    }

    public void setPolls(List<Poll> polls) {
        this.polls = polls;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    public List<Tweet> getTweets() {
        return tweets;
    }

    public void setTweets(List<Tweet> tweets) {
        this.tweets = tweets;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}

class Media {
    public int height;
    public String media_key;
    public String type;
    public int width;
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public String getMedia_key() {
        return media_key;
    }
    public void setMedia_key(String media_key) {
        this.media_key = media_key;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    
}

class Place {
    public List<String> contained_within;
    public String country;
    public String country_code;
    public String full_name;
    public Geo geo;
    public String id;
    public String name;
    public String place_type;
    public List<String> getContained_within() {
        return contained_within;
    }
    public void setContained_within(List<String> contained_within) {
        this.contained_within = contained_within;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getCountry_code() {
        return country_code;
    }
    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }
    public String getFull_name() {
        return full_name;
    }
    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }
    public Geo getGeo() {
        return geo;
    }
    public void setGeo(Geo geo) {
        this.geo = geo;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPlace_type() {
        return place_type;
    }
    public void setPlace_type(String place_type) {
        this.place_type = place_type;
    }
    
}

class Geo {
    public List<Double> bbox;
    public Geometry geometry;
    public Object properties;
    public String type;
    public List<Double> getBbox() {
        return bbox;
    }
    public void setBbox(List<Double> bbox) {
        this.bbox = bbox;
    }
    public Geometry getGeometry() {
        return geometry;
    }
    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }
    public Object getProperties() {
        return properties;
    }
    public void setProperties(Object properties) {
        this.properties = properties;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    
}

class Geometry {
    public List<Double> coordinates;
    public String type;
    public List<Double> getCoordinates() {
        return coordinates;
    }
    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    
}

class Poll {
    public int duration_minutes;
    public String end_datetime;
    public String id;
    public List<PollOption> options;
    public String voting_status;
    public int getDuration_minutes() {
        return duration_minutes;
    }
    public void setDuration_minutes(int duration_minutes) {
        this.duration_minutes = duration_minutes;
    }
    public String getEnd_datetime() {
        return end_datetime;
    }
    public void setEnd_datetime(String end_datetime) {
        this.end_datetime = end_datetime;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public List<PollOption> getOptions() {
        return options;
    }
    public void setOptions(List<PollOption> options) {
        this.options = options;
    }
    public String getVoting_status() {
        return voting_status;
    }
    public void setVoting_status(String voting_status) {
        this.voting_status = voting_status;
    }
    
}

class PollOption {
    public String label;
    public int position;
    public int votes;
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public int getPosition() {
        return position;
    }
    public void setPosition(int position) {
        this.position = position;
    }
    public int getVotes() {
        return votes;
    }
    public void setVotes(int votes) {
        this.votes = votes;
    }
    
}

class Topic {
    public String description;
    public String id;
    public String name;
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
}

class User {
    public String created_at;
    public String id;
    public String name;
    public boolean protected_;
    public String username;
    public String getCreated_at() {
        return created_at;
    }
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isProtected_() {
        return protected_;
    }
    public void setProtected_(boolean protected_) {
        this.protected_ = protected_;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    
}

class Meta {
    public String newest_id;
    public String next_token;
    public String oldest_id;
    public String previous_token;
    public int result_count;
    public String getNewest_id() {
        return newest_id;
    }
    public void setNewest_id(String newest_id) {
        this.newest_id = newest_id;
    }
    public String getNext_token() {
        return next_token;
    }
    public void setNext_token(String next_token) {
        this.next_token = next_token;
    }
    public String getOldest_id() {
        return oldest_id;
    }
    public void setOldest_id(String oldest_id) {
        this.oldest_id = oldest_id;
    }
    public String getPrevious_token() {
        return previous_token;
    }
    public void setPrevious_token(String previous_token) {
        this.previous_token = previous_token;
    }
    public int getResult_count() {
        return result_count;
    }
    public void setResult_count(int result_count) {
        this.result_count = result_count;
    }
    
}
