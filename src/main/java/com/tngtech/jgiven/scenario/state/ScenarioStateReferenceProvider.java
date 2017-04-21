package com.tngtech.jgiven.scenario.state;

import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.light.LightMemberReference;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.search.searches.AnnotatedElementsSearch;
import com.intellij.psi.util.PsiTypesUtil;
import com.intellij.util.Processor;
import com.tngtech.jgiven.resolution.ResolutionHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.tngtech.jgiven.Annotations.getScenarioStateClasses;

public class ScenarioStateReferenceProvider {
    static final int ANY_NUMBER_OF_RESULTS = -1;

    public List<PsiReference> findReferences(PsiField field, int maxNumberOfResults) {
        PsiClass fieldClass = PsiTypesUtil.getPsiClass(field.getType());
        if (fieldClass == null) {
            return Collections.emptyList();
        }
        Project project = field.getProject();
        PsiManager manager = PsiManager.getInstance(project);
        PsiReferenceProcessor processor = new PsiReferenceProcessor(field, maxNumberOfResults, manager);

        SearchScope scope = GlobalSearchScope.everythingScope(project).intersectWith(javaFilesScope(project));

        findPsiFields(project, (GlobalSearchScope) scope, processor);
        return processor.results;
    }

    @NotNull
    private GlobalSearchScope javaFilesScope(Project project) {
        return GlobalSearchScope.getScopeRestrictedByFileTypes(GlobalSearchScope.allScope(project), StdFileTypes.JAVA);
    }

    private void findPsiFields(Project project, GlobalSearchScope scope, Processor<PsiField> processor) {
        getScenarioStateClasses(project)
                .forEach(a -> AnnotatedElementsSearch.searchPsiFields(a, scope).forEach(processor));
    }

    public List<PsiReference> findReferences(PsiField field) {
        return findReferences(field, ANY_NUMBER_OF_RESULTS);
    }

    private static class PsiReferenceProcessor implements Processor<PsiField> {

        private ScenarioStateAnnotationProvider scenarioStateProvider = new ScenarioStateAnnotationProvider();
        private ResolutionHandler resolutionHandler = new ResolutionHandler();
        private PsiField fieldToSearch;
        private int maxNumberOfResults;
        private PsiManager manager;
        private List<PsiReference> results = new ArrayList<>();

        PsiReferenceProcessor(PsiField fieldToSearch, int maxNumberOfResults, PsiManager manager) {
            this.fieldToSearch = fieldToSearch;
            this.maxNumberOfResults = maxNumberOfResults;
            this.manager = manager;
        }

        @Override
        public boolean process(PsiField field) {
            if (scenarioStateProvider.isJGivenScenarioState(field)
                    && !fieldToSearch.equals(field)
                    && resolutionHandler.resolutionMatches(field, fieldToSearch)) {

                results.add(new LightMemberReference(manager, field, PsiSubstitutor.EMPTY) {
                    @Override
                    public PsiElement getElement() {
                        return field;
                    }
                });
            }
            return results.size() < maxNumberOfResults || maxNumberOfResults == ANY_NUMBER_OF_RESULTS;
        }
    }
}
