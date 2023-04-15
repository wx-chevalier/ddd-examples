package icu.ngte.udma.core.tools.modelmapper;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import icu.ngte.udma.core.tools.ds.JsonTools;
import icu.ngte.udma.core.type.model.HasId;
import icu.ngte.udma.core.tools.phonenumber.PhoneNumberTools;
import io.vavr.API;
import io.vavr.collection.List;
import io.vavr.control.Try;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;

@Slf4j
public class ModelMapperTools {

  private static final ModelMapper mm = new ModelMapper();

  public static <S, T> T map(S src, Class<T> dstClass) {
    return mm.map(src, dstClass);
  }

  public static final Converter<String, ObjectNode> STRING_TO_OBJECT_NODE_IGNORES_INVALID =
      context -> {
        String source = context.getSource();
        if (source == null) {
          return null;
        } else {
          ObjectNode res = JsonTools.fromString(source, ObjectNode.class);
          if (res == null) {
            log.warn("[FAILED][String -> ObjectNode] [{}]", source);
            return null;
          } else {
            return res;
          }
        }
      };

  public static final Converter<ObjectNode, String> OBJECT_NODE_TO_STRING =
      context -> API.Option(context.getSource()).map(JsonTools::toString).getOrNull();

  public static void registerPhoneNumberConverter(
      ModelMapper mm, PhoneNumberFormat phoneNumberFormat) {
    mm.addConverter(
        context ->
            API.Option(context.getSource())
                .map(v -> PhoneNumberTools.format(v, phoneNumberFormat))
                .getOrNull(),
        PhoneNumber.class,
        String.class);
    mm.addConverter(
        context ->
            API.Option(context.getSource()).map(PhoneNumberTools::parse).map(Try::get).getOrNull(),
        String.class,
        PhoneNumber.class);
  }

  public static void registerUUIDConverter() {
    mm.addConverter(
        context -> API.Option(context.getSource()).map(UUID::toString).getOrNull(),
        UUID.class,
        String.class);
    mm.addConverter(
        context -> API.Option(context.getSource()).map(UUID::fromString).getOrNull(),
        String.class,
        UUID.class);
  }

  public static void registerPhoneNumberConverter(ModelMapper mm) {
    registerPhoneNumberConverter(mm, PhoneNumberFormat.E164);
  }

  public static <T extends HasId<UUID>> void registerUUIDBasedIdConverter(
      ModelMapper mm, Class<T> clazz, Function<UUID, T> constructor) {
    mm.addConverter(
        context -> API.Option(context.getSource()).map(T::getId).getOrNull(), clazz, UUID.class);
    mm.addConverter(
        context -> API.Option(context.getSource()).map(constructor).getOrNull(), UUID.class, clazz);

    mm.addConverter(
        context -> API.Option(context.getSource()).map(T::getId).map(UUID::toString).getOrNull(),
        clazz,
        String.class);
    mm.addConverter(
        context ->
            API.Option(context.getSource()).map(UUID::fromString).map(constructor).getOrNull(),
        String.class,
        clazz);
  }

  public static <T extends HasId<Long>> void registerLongBasedIdConverter(
      ModelMapper mm, Class<T> clazz, Function<Long, T> constructor) {
    mm.addConverter(
        context -> API.Option(context.getSource()).map(T::getId).getOrNull(), clazz, Long.class);
    mm.addConverter(
        context -> API.Option(context.getSource()).map(constructor).getOrNull(), Long.class, clazz);
  }

  public static <T, S> void registerConverters(
      ModelMapper mm,
      List<Class<? extends T>> srcClasses,
      Class<S> dstClass,
      Function<T, S> converter) {
    for (Class<? extends T> srcClass : srcClasses) {
      mm.addConverter(context -> converter.apply(context.getSource()), srcClass, dstClass);
    }
  }

  public static <T, S, TF, SF> void registerPropertyMapping(
      ModelMapper mm,
      Class<T> t,
      Function<T, TF> tfGetter,
      BiConsumer<T, TF> tfSetter,
      Class<S> s,
      Function<S, SF> sfGetter,
      BiConsumer<S, SF> sfSetter) {
    mm.typeMap(s, t).addMappings(mapping -> mapping.map(sfGetter::apply, tfSetter::accept));
    mm.typeMap(t, s).addMappings(mapping -> mapping.map(tfGetter::apply, sfSetter::accept));
  }

  @SuppressWarnings("unchecked")
  public static <T, S, TF, SF> void registerPropertyMapping(
      ModelMapper mm,
      Class<T> t,
      Function<T, TF> tfGetter,
      BiConsumer<T, TF> tfSetter,
      Class<S> s,
      Function<S, SF> sfGetter,
      BiConsumer<S, SF> sfSetter,
      Function<TF, SF> convertTfieldToSfield,
      Function<SF, TF> convertSfieldToTfield) {
    mm.typeMap(s, t)
        .addMappings(
            mapping ->
                mapping
                    .using(context -> convertSfieldToTfield.apply((SF) context.getSource()))
                    .map(sfGetter::apply, tfSetter::accept));
    mm.typeMap(t, s)
        .addMappings(
            mapping ->
                mapping
                    .using(context -> convertTfieldToSfield.apply((TF) context.getSource()))
                    .map(tfGetter::apply, sfSetter::accept));
  }
}
