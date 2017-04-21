package com.tngtech.jgiven.line.marker;

import com.google.common.collect.Iterables;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.impl.LineMarkerNavigator;
import com.intellij.codeInsight.daemon.impl.MarkerType;
import com.intellij.codeInsight.daemon.impl.PsiElementListNavigator;
import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.FileIndexFacade;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.ProjectScopeImpl;
import com.tngtech.jgiven.scenario.state.ScenarioStateAnnotationProvider;
import com.tngtech.jgiven.scenario.state.ScenarioStateReferenceProvider;
import com.tngtech.jgiven.util.PsiElementUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class LineMarkerProvider implements com.intellij.codeInsight.daemon.LineMarkerProvider {
    private static final String UNKNOWN_TEXT = "JGiven State (too many references)";
    private ScenarioStateAnnotationProvider scenarioStateProvider = new ScenarioStateAnnotationProvider();
    private ScenarioStateReferenceProvider scenarioStateReferenceProvider = new ScenarioStateReferenceProvider();

    @Nullable
    @Override
    public MyLineMarkerInfo getLineMarkerInfo(@NotNull PsiElement element) {
        return null;
    }

    @Nullable
    private MyLineMarkerInfo collectMarkerFor(@NotNull PsiElement element) {
        if (!scenarioStateProvider.isJGivenScenarioState(element)) {
            return null;
        }
        PsiField field = (PsiField) element;
        if (scenarioStateReferenceProvider.isTooGeneric(field)) {
            return new MyLineMarkerInfo(element, Icons.JGIVEN_UNKNOWN, new MarkerType("jgiven-unknown", (e) -> UNKNOWN_TEXT, doNotNavigateNavigator()), UNKNOWN_TEXT);
        }
        List<PsiField> references = allReferencingFields(field);
        if (references.isEmpty()) {
            return null;
        }

        return new MyLineMarkerInfo(element, Icons.JGIVEN, new MarkerType("jgiven", (e) -> "JGiven States", navigatorToEements()), "JGiven States");
    }

    @NotNull
    private LineMarkerNavigator navigatorToEements() {
        return new LineMarkerNavigator() {

            @Override
            public void browse(MouseEvent e, PsiElement element) {
                PsiElementListNavigator.openTargets(e, Iterables.toArray(allReferencingFields((PsiField) element), PsiField.class),
                        "JGiven States", "", new DefaultPsiElementCellRenderer());
            }
        };
    }

    @NotNull
    private LineMarkerNavigator doNotNavigateNavigator() {
        return new LineMarkerNavigator() {

            @Override
            public void browse(MouseEvent e, PsiElement element) {
                PsiElementListNavigator.openTargets(e, new PsiField[0],
                        "JGiven States", "", new DefaultPsiElementCellRenderer());
            }
        };
    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<PsiElement> elements, @NotNull Collection<LineMarkerInfo> result) {
        elements.stream()
                .map(this::collectMarkerFor)
                .filter(Objects::nonNull)
                .forEach(result::add);
    }

    private List<PsiField> allReferencingFields(@NotNull PsiField element) {
        Project project = element.getProject();
        return scenarioStateReferenceProvider.findReferences(new ProjectScopeImpl(project, FileIndexFacade.getInstance(project)), element, 10)
                .stream().map(this::fieldOf)
                .collect(Collectors.toList());
    }

    private PsiField fieldOf(PsiReference r) {
        return PsiElementUtil.findParentOfTypeOn(r.getElement(), PsiField.class).orElse(null);
    }
}
