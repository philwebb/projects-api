package io.spring.projectapi;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.spring.projectapi.github.GithubOperations;
import io.spring.projectapi.github.Project;
import io.spring.projectapi.github.ProjectDocumentation;
import io.spring.projectapi.github.ProjectSupport;
import io.spring.projectapi.web.CacheController;

import org.springframework.stereotype.Component;

/**
 * Caches Github project information. Populated on start up and updates triggered via
 * {@link CacheController}.
 */
@Component
public class ProjectRepository {

	private final GithubOperations githubOperations;

	private transient Data data;

	public ProjectRepository(GithubOperations githubOperations) {
		this.githubOperations = githubOperations;
		this.data = Data.load(githubOperations);
	}

	public void update() {
		this.data = Data.load(this.githubOperations);
	}

	public Collection<Project> getProjects() {
		return this.data.project().values();
	}

	public Project getProject(String projectSlug) {
		return this.data.project().get(projectSlug);
	}

	public List<ProjectDocumentation> getProjectDocumentations(String projectSlug) {
		return this.data.documentation().get(projectSlug);
	}

	public List<ProjectSupport> getProjectSupports(String projectSlug) {
		return this.data.support().get(projectSlug);
	}

	public String getProjectSupportPolicy(String projectSlug) {
		return this.data.supportPolicy().get(projectSlug);
	}

	record Data(Map<String, Project> project, Map<String, List<ProjectDocumentation>> documentation,
			Map<String, List<ProjectSupport>> support, Map<String, String> supportPolicy) {

		public static Data load(GithubOperations githubOperations) {
			Map<String, Project> projects = new LinkedHashMap<>();
			Map<String, List<ProjectDocumentation>> documentation = new LinkedHashMap<>();
			Map<String, List<ProjectSupport>> support = new LinkedHashMap<>();
			Map<String, String> supportPolicy = new LinkedHashMap<>();
			githubOperations.getProjects().forEach((project) -> {
				String slug = project.getSlug();
				projects.put(slug, project);
				documentation.put(slug, githubOperations.getProjectDocumentations(slug));
				support.put(slug, githubOperations.getProjectSupports(slug));
				supportPolicy.put(slug, githubOperations.getProjectSupportPolicy(slug));
			});
			return new Data(Map.copyOf(projects), Map.copyOf(documentation), Map.copyOf(support),
					Map.copyOf(supportPolicy));
		}

	}

}
