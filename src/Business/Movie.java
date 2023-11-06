package Business;


import java.time.LocalDate;

public class Movie {

	private int movieId = 0;
	private String title;
	private String releaseDate;
	private float avgRating;
	private String genre;
	private String staff;
	private String description;


	public int getMovieId() {
		return movieId;
	}
	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public float getAvgRating() {
		return avgRating;
	}
	public void setAvgRating(float avgRating) {
		this.avgRating = avgRating;
	}

	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getPrimaryGenre() {
		return (genre.split(",", 2).length > 0) ? genre.split(",", 2)[0] : null;
	}
	public String getSecondaryGenre() {
		return (genre.split(",", 2).length > 1) ? genre.split(",", 2)[1] : null;
	}

	public String getStaff() {
		return staff;
	}
	public void setStaff(String staff) {
		this.staff = staff;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String toString()
	{
		return getTitle();
	}
}
