package com.tngtech.jgiven.scenario.state;

import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTypesUtil;
import com.intellij.util.Processor;
import com.tngtech.jgiven.resolution.ResolutionHandler;
import com.tngtech.jgiven.util.PsiElementUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.intellij.psi.search.GlobalSearchScopesCore.projectTestScope;

public class ScenarioStateReferenceProvider {
    static final int ANY_NUMBER_OF_RESULTS = -1;

    public List<PsiReference> findReferences(SearchScope scope, PsiField field, int maxNumberOfResults) {
        PsiClass fieldClass = PsiTypesUtil.getPsiClass(field.getType());
        if (fieldClass == null) {
            return Collections.emptyList();
        }
        Project project = field.getProject();
        PsiReferenceProcessor processor = new PsiReferenceProcessor(field, maxNumberOfResults);
        GlobalSearchScope javaFilesScope = GlobalSearchScope.getScopeRestrictedByFileTypes(GlobalSearchScope.allScope(project), StdFileTypes.JAVA);

        scope = scope.intersectWith(projectTestScope(project)).intersectWith(javaFilesScope);

        ReferencesSearch.search(fieldClass, scope).forEach(processor);
        return processor.results;
    }

    public List<PsiReference> findReferences(SearchScope scope, PsiField field) {
        return findReferences(scope, field, ANY_NUMBER_OF_RESULTS);
    }

    public boolean isTooGeneric(PsiField field) {
        PsiClass clazz = PsiTypesUtil.getPsiClass(field.getType());
        if (clazz == null) {
            return true;
        }
        String name = clazz.getQualifiedName();
        return name == null
                || name.startsWith("java.")
                || name.startsWith("com.google");
    }

    private static class PsiReferenceProcessor implements Processor<PsiReference> {

        private ScenarioStateAnnotationProvider scenarioStateProvider = new ScenarioStateAnnotationProvider();
        private ResolutionHandler resolutionHandler = new ResolutionHandler();
        private PsiField fieldToSearch;
        private int maxNumberOfResults;
        private List<PsiReference> results = new ArrayList<>();

        PsiReferenceProcessor(PsiField fieldToSearch, int maxNumberOfResults) {
            this.fieldToSearch = fieldToSearch;
            this.maxNumberOfResults = maxNumberOfResults;
        }

        @Override
        public boolean process(PsiReference psiReference) {
            PsiField field = fieldOf(psiReference);
            if (scenarioStateProvider.isJGivenScenarioState(field)
                    && !fieldToSearch.equals(field)
                    && resolutionHandler.resolutionMatches(field, fieldToSearch)) {

                results.add(psiReference);
            }
            return results.size() < maxNumberOfResults || maxNumberOfResults == ANY_NUMBER_OF_RESULTS;
        }

        private PsiField fieldOf(PsiReference r) {
            return PsiElementUtil.findParentOfTypeOn(r.getElement(), PsiField.class).orElse(null);
        }
    }
}
