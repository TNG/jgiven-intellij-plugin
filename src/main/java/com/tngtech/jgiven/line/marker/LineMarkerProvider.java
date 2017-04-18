package com.tngtech.jgiven.line.marker;

import com.google.common.collect.Iterables;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.impl.LineMarkerNavigator;
import com.intellij.codeInsight.daemon.impl.MarkerType;
import com.intellij.codeInsight.daemon.impl.PsiElementListNavigator;
import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiReference;
import com.tngtech.jgiven.scenario.state.ScenarioStateAnnotationProvider;
import com.tngtech.jgiven.scenario.state.ScenarioStateReferenceProvider;
import com.tngtech.jgiven.util.PsiElementUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class LineMarkerProvider implements com.intellij.codeInsight.daemon.LineMarkerProvider {
    private ScenarioStateAnnotationProvider scenarioStateProvider = new ScenarioStateAnnotationProvider();
    private ScenarioStateReferenceProvider scenarioStateReferenceProvider = new ScenarioStateReferenceProvider();

    @Nullable
    @Override
    public com.intellij.codeInsight.daemon.LineMarkerInfo getLineMarkerInfo(@NotNull PsiElement element) {
        if (!scenarioStateProvider.isJGivenScenarioState(element)) {
            return null;
        }
        List<PsiField> references = allReferencingFields((PsiField) element);
        if (references.isEmpty()) {
            return null;
        }

        return new com.tngtech.jgiven.line.marker.LineMarkerInfo(element, Icons.JGIVEN, new MarkerType("jgiven", (e) -> "JGiven States", new LineMarkerNavigator() {

            @Override
            public void browse(MouseEvent e, PsiElement element) {
                PsiElementListNavigator.openTargets(e, Iterables.toArray(allReferencingFields((PsiField) element), PsiField.class),
                        "JGiven States", "", new DefaultPsiElementCellRenderer());
            }
        }), "JGiven States");
    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<PsiElement> elements, @NotNull Collection<LineMarkerInfo> result) {
    }

    private List<PsiField> allReferencingFields(@NotNull PsiField element) {
        return scenarioStateReferenceProvider.findReferences(element)
                .stream().map(this::fieldOf)
                .collect(Collectors.toList());
    }

    private PsiField fieldOf(PsiReference r) {
        return PsiElementUtil.findParentOfTypeOn(r.getElement(), PsiField.class).orElse(null);
    }
}
