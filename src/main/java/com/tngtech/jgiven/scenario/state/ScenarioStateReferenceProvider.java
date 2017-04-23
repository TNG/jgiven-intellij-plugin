package com.tngtech.jgiven.scenario.state;

import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.search.searches.AnnotatedElementsSearch;
import com.intellij.psi.util.PsiTypesUtil;
import com.intellij.util.Processor;
import com.tngtech.jgiven.resolution.ResolutionHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import static com.tngtech.jgiven.Annotations.getScenarioStateClasses;

public class ScenarioStateReferenceProvider {
    static final int ANY_NUMBER_OF_RESULTS = -1;
    private ScenarioStateAnnotationProvider scenarioStateProvider = new ScenarioStateAnnotationProvider();
    private ResolutionHandler resolutionHandler = new ResolutionHandler();

    public List<PsiReference> findReferences(PsiField field, int maxNumberOfResults) {
        PsiClass fieldClass = PsiTypesUtil.getPsiClass(field.getType());
        if (fieldClass == null) {
            return Collections.emptyList();
        }
        Project project = field.getProject();
        PsiManager manager = PsiManager.getInstance(project);
        JGivenUsageProvider usageProvider = new JGivenUsageProvider(scenarioStateProvider, resolutionHandler, new ReferenceFactory(manager));
        StateReferenceProcessor processor = new StateReferenceProcessor(field, maxNumberOfResults, usageProvider);

        SearchScope scope = GlobalSearchScope.everythingScope(project).intersectWith(javaFilesScope(project));

        findPsiFields(project, (GlobalSearchScope) scope, processor);
        return processor.getResults();
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

}
