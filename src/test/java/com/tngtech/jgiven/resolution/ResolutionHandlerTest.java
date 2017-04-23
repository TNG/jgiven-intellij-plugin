package com.tngtech.jgiven.resolution;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static com.tngtech.jgiven.resolution.Resolution.NAME;
import static com.tngtech.jgiven.resolution.Resolution.TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResolutionHandlerTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private ResolutionProvider resolutionProvider;
    @Mock
    private PsiField field1;
    @Mock
    private PsiField field2;

    @InjectMocks
    private ResolutionHandler resolutionHandler;

    @Test
    public void should_handle_equal_field_names() throws Exception {
        when(resolutionProvider.getResolutionFrom(field1)).thenReturn(NAME);
        when(resolutionProvider.getResolutionFrom(field2)).thenReturn(NAME);
        when(field1.getName()).thenReturn("field");
        when(field2.getName()).thenReturn("field");

        boolean result = resolutionHandler.resolutionMatches(field1, field2);

        assertThat(result).isTrue();
    }

    @Test
    public void should_handle_not_equal_field_names() throws Exception {
        when(resolutionProvider.getResolutionFrom(field1)).thenReturn(NAME);
        when(resolutionProvider.getResolutionFrom(field2)).thenReturn(NAME);
        when(field1.getName()).thenReturn("field1");
        when(field2.getName()).thenReturn("field2");

        boolean result = resolutionHandler.resolutionMatches(field1, field2);

        assertThat(result).isFalse();
    }

    @Test
    public void should_handle_equal_types() throws Exception {
        when(resolutionProvider.getResolutionFrom(field1)).thenReturn(TYPE);
        when(resolutionProvider.getResolutionFrom(field2)).thenReturn(TYPE);
        mockPresentableTextWith(field1, "type");
        mockPresentableTextWith(field2, "type");

        boolean result = resolutionHandler.resolutionMatches(field1, field2);

        assertThat(result).isTrue();
    }

    @Test
    public void should_handle_not_equal_types() throws Exception {
        when(resolutionProvider.getResolutionFrom(field1)).thenReturn(TYPE);
        when(resolutionProvider.getResolutionFrom(field2)).thenReturn(TYPE);
        mockPresentableTextWith(field1, "type1");
        mockPresentableTextWith(field2, "type2");

        boolean result = resolutionHandler.resolutionMatches(field1, field2);

        assertThat(result).isFalse();
    }

    @Test
    public void should_handle_different_resolution() throws Exception {
        when(resolutionProvider.getResolutionFrom(field1)).thenReturn(NAME);
        when(resolutionProvider.getResolutionFrom(field2)).thenReturn(TYPE);

        boolean result = resolutionHandler.resolutionMatches(field1, field2);

        assertThat(result).isFalse();
    }

    private void mockPresentableTextWith(PsiField field, String text) {
        PsiType type = mock(PsiType.class);
        when(field.getType()).thenReturn(type);
        when(type.getPresentableText()).thenReturn(text);
    }
}